package com.outh.backend.services;

import com.outh.backend.dto.UserBetDTO;
import com.outh.backend.models.Bets;
import com.outh.backend.models.BetStatus;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.User;
import com.outh.backend.repository.BetRepository;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final LeagueMatchesRepository leagueMatchesRepository;
    private final UserRepository userRepository;

    public BetService(BetRepository betRepository, UserRepository userRepository,LeagueMatchesRepository leagueMatchesRepository) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        this.leagueMatchesRepository = leagueMatchesRepository;
    }

    @Transactional
    public Bets placeBet(String firebaseId, String matchId, String outcome, BigDecimal betAmount, BigDecimal expectedWinAmount) {
        User user = userRepository.findById(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAccount_balance().compareTo(betAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct from user balance
        user.setAccount_balance(user.getAccount_balance().subtract(betAmount));
        userRepository.save(user);

        // Create bet
        Bets bet = new Bets();
        bet.setUser(user);
        bet.setMatchId(matchId);
        bet.setOutcome(outcome);
        bet.setBetAmount(betAmount);
        bet.setExpectedWinAmount(expectedWinAmount);
        bet.setStatus(BetStatus.PENDING);

        return betRepository.save(bet);
    }

    @Transactional
    public void settlePendingBets() {
        List<Bets> pendingBets = betRepository.findByStatus(BetStatus.PENDING);

        for (Bets bet : pendingBets) {
            LeagueMatches match = leagueMatchesRepository.findById(Long.valueOf(bet.getMatchId()))
                    .orElse(null);

            if (match == null || !"FT".equalsIgnoreCase(match.getMatchStatus())) {
                continue; // Skip matches not finished
            }

            String actualOutcome;
            if (match.getHomeScore() > match.getAwayScore()) {
                actualOutcome = "homewin";
            } else if (match.getHomeScore() < match.getAwayScore()) {
                actualOutcome = "awaywin";
            } else {
                actualOutcome = "draw";
            }

            if (actualOutcome.equalsIgnoreCase(bet.getOutcome())) {
                // User won
                bet.setStatus(BetStatus.WON);
                User user = bet.getUser();
                user.setAccount_balance(
                        user.getAccount_balance().add(bet.getExpectedWinAmount())
                );
                userRepository.save(user);
            } else {
                bet.setStatus(BetStatus.LOST);
            }

            betRepository.save(bet);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserBetStats(String firebaseId) {
        User user = userRepository.findById(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Bets> userBets = betRepository.findByUser(user);

        int totalBets = userBets.size();
        long wonBets = userBets.stream().filter(b -> b.getStatus() == BetStatus.WON).count();
        BigDecimal totalBetAmount = userBets.stream()
                .map(Bets::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProfit = userBets.stream()
                .filter(b -> b.getStatus() == BetStatus.WON) // only count won bets
                .map(b -> b.getExpectedWinAmount().subtract(b.getBetAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BigDecimal averageBet = totalBets > 0
                ? totalBetAmount.divide(BigDecimal.valueOf(totalBets), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        double winRate = totalBets > 0
                ? (wonBets * 100.0) / totalBets
                : 0.0;

        return Map.of(
                "totalBetsPlaced", totalBets,
                "winRate", String.format("%.2f", winRate),
                "averageBet", averageBet,
                "totalProfit", totalProfit
        );
    }

    public List<Bets> getBetsByUser(String firebaseId) {
        // Ensure user exists
        userRepository.findById(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return all bets for the user
        return betRepository.findAllByUser_FirebaseId(firebaseId);
    }


    public List<UserBetDTO> getUserBets(String firebaseId) {
        User user = userRepository.findById(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Bets> userBets = betRepository.findByUser(user);

        return userBets.stream().map(bet -> {
            LeagueMatches match = leagueMatchesRepository.findById(Long.valueOf(bet.getMatchId()))
                    .orElseThrow(() -> new RuntimeException("Match not found for bet"));

            String matchString = match.getHomeTeam() + " vs " + match.getAwayTeam();

            return new UserBetDTO(
                    matchString,
                    bet.getOutcome(),
                    bet.getBetAmount(),
                    bet.getExpectedWinAmount(),
                    bet.getStatus().name(),
                    bet.getCreatedAt(),
                    match.getHomeTeam(),
                    match.getAwayTeam()
            );
        }).collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {

        settlePendingBets();

        System.out.println("Team matches at startup!");
    }

}
