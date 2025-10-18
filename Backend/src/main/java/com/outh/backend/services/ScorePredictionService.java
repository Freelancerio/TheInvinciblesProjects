package com.outh.backend.services;

import com.outh.backend.dto.LeaderboardEntryDTO;
import com.outh.backend.dto.ExposedApiDTO;
import com.outh.backend.dto.ScorePredictionRequest;
import com.outh.backend.models.*;
import com.outh.backend.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScorePredictionService {

    private final ScorePredictionRepository scorePredictionRepository;
    private final UserRepository userRepository;
    private final LeagueMatchesRepository matchRepository;
    private final SeasonLeaderboardRepository leaderboardRepository;
    private final AllTimeLeaderboardRepository allTimeLeaderboardRepository;

    public ScorePredictionService(
            ScorePredictionRepository scorePredictionRepository,
            UserRepository userRepository,
            LeagueMatchesRepository matchRepository, SeasonLeaderboardRepository leaderboardRepository,
            AllTimeLeaderboardRepository allTimeLeaderboardRepository
    ) {
        this.scorePredictionRepository = scorePredictionRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.leaderboardRepository = leaderboardRepository;
        this.allTimeLeaderboardRepository = allTimeLeaderboardRepository;
    }

    public ScorePrediction savePrediction(ScorePredictionRequest request) {
        User user = userRepository.findByFirebaseId(request.getFirebaseId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeagueMatches match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Match not found"));

        // Check for existing prediction
        scorePredictionRepository.findByUserAndMatch(user, match).ifPresent(existing -> {
            throw new RuntimeException("Prediction already exists for this user and match");
        });

        ScorePrediction prediction = new ScorePrediction(
                user,
                match,
                request.getSeason(),
                request.getPredHomeScore(),
                request.getPredAwayScore()
        );

        return scorePredictionRepository.save(prediction);
    }


    /**
     * Update points for all predictions where the related match has finished (FT).
     */
    @Transactional
    public void updatePointsForFinishedMatches() {
        List<ScorePrediction> predictions = scorePredictionRepository.findAll();

        for (ScorePrediction prediction : predictions) {
            LeagueMatches match = prediction.getMatch();

            // Only calculate if match is finished
            if ("FT".equalsIgnoreCase(match.getMatchStatus())) {
                int actualHome = match.getHomeScore();
                int actualAway = match.getAwayScore();

                int predictedHome = prediction.getPredHomeScore();
                int predictedAway = prediction.getPredAwayScore();

                int points = calculatePoints(predictedHome, predictedAway, actualHome, actualAway);

                prediction.setPoints(points);
                scorePredictionRepository.save(prediction);
            }
        }
    }

    /**
     * Points system (you can customize this):
     *  - Exact score correct → 3 points
     *  - Correct outcome (win/draw/loss) but wrong score → 1 point
     *  - Wrong outcome → 0 points
     */
    int calculatePoints(int predHome, int predAway, int actualHome, int actualAway) {
        // Exact score
        if (predHome == actualHome && predAway == actualAway) {
            return 5;
        }

        int predDiff = Integer.compare(predHome, predAway);
        int actualDiff = Integer.compare(actualHome, actualAway);

        int points;

        if (predDiff == actualDiff) {
            // Correct outcome but not exact score
            points = 2;
        } else {
            // Wrong outcome
            points = 0;
        }

        // Check if predicted score is far off (2 or more goals difference)
        int homeDiff = Math.abs(predHome - actualHome);
        int awayDiff = Math.abs(predAway - actualAway);

        if (homeDiff >= 2 || awayDiff >= 2) {
            points -= 1; // Apply penalty
        }

        return points;
    }


    /**
     * Update the season leaderboard by aggregating points per user per season.
     */
    @Transactional
    public void updateSeasonLeaderboard() {
        // Fetch all predictions with points set (match already played)
        List<ScorePrediction> predictions = scorePredictionRepository.findAll()
                .stream()
                .filter(p -> p.getPoints() != null)
                .collect(Collectors.toList());

        // Group by user + season
        Map<String, List<ScorePrediction>> grouped = predictions.stream()
                .collect(Collectors.groupingBy(p -> p.getUser().getFirebaseId() + "-" + p.getSeason()));

        for (Map.Entry<String, List<ScorePrediction>> entry : grouped.entrySet()) {
            List<ScorePrediction> userPredictions = entry.getValue();
            ScorePrediction first = userPredictions.get(0);
            String userId = first.getUser().getFirebaseId();
            Integer season = first.getSeason();

            // Sum points
            int totalPoints = userPredictions.stream()
                    .mapToInt(ScorePrediction::getPoints)
                    .sum();

            // Fetch existing leaderboard entry or create new
            SeasonLeaderboard leaderboard = leaderboardRepository
                    .findByUser_FirebaseIdAndSeason(userId, season)
                    .orElseGet(() -> {
                        SeasonLeaderboard sl = new SeasonLeaderboard();
                        sl.setUser(first.getUser());
                        sl.setSeason(season);
                        return sl;
                    });


            leaderboard.setPoints(totalPoints);
            leaderboardRepository.save(leaderboard);
        }
    }

    /**
     * Update the all-time leaderboard by aggregating points per user.
     */
    @Transactional
    public void updateAllTimeLeaderboard() {
        // Fetch all predictions with points set
        List<ScorePrediction> predictions = scorePredictionRepository.findAll()
                .stream()
                .filter(p -> p.getPoints() != null)
                .collect(Collectors.toList());

        // Group by user
        Map<String, List<ScorePrediction>> grouped = predictions.stream()
                .collect(Collectors.groupingBy(p -> p.getUser().getFirebaseId()));

        for (Map.Entry<String, List<ScorePrediction>> entry : grouped.entrySet()) {
            List<ScorePrediction> userPredictions = entry.getValue();
            User user = userPredictions.get(0).getUser();
            String firebaseId = user.getFirebaseId();

            // Sum points
            int totalPoints = userPredictions.stream()
                    .mapToInt(ScorePrediction::getPoints)
                    .sum();

            // Fetch existing all-time leaderboard entry or create new
            AllTimeLeaderboard leaderboard = allTimeLeaderboardRepository
                    .findByUser_FirebaseId(firebaseId)
                    .orElseGet(() -> {
                        AllTimeLeaderboard al = new AllTimeLeaderboard();
                        al.setUser(user);
                        return al;
                    });

            leaderboard.setPoints(totalPoints);
            allTimeLeaderboardRepository.save(leaderboard);
        }

    }

    public List<LeaderboardEntryDTO> getSeasonLeaderboard(Integer season) {
        return leaderboardRepository.findBySeasonOrderByPointsDesc(season)
                .stream()
                .map(sl -> new LeaderboardEntryDTO(
                        sl.getUser().getUsername(),
                        sl.getPoints(),
                        sl.getSeason()
                ))
                .collect(Collectors.toList());
    }

    public List<LeaderboardEntryDTO> getAllTimeLeaderboard() {
        return allTimeLeaderboardRepository.findAllByOrderByPointsDesc()
                .stream()
                .map(al -> new LeaderboardEntryDTO(
                        al.getUser().getUsername(),
                        al.getPoints(),
                        null
                ))
                .collect(Collectors.toList());
    }

    public int getUserAllTimePosition(String firebaseId) {
        // Fetch all leaderboard entries ordered by points desc
        List<AllTimeLeaderboard> leaderboard = allTimeLeaderboardRepository.findAllByOrderByPointsDesc();

        // Loop through and find index
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).getUser().getFirebaseId().equals(firebaseId)) {
                return i + 1; // position is index+1
            }
        }

        throw new RuntimeException("User not found in leaderboard");
    }

    public double getUserAllTimeAccuracy(String firebaseId) {
        // Fetch user
        User user = userRepository.findByFirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all-time points
        AllTimeLeaderboard allTimeEntry = allTimeLeaderboardRepository.findByUser_FirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found in all-time leaderboard"));

        int totalPoints = allTimeEntry.getPoints();

        // Count number of predictions made
        long totalPredictions = scorePredictionRepository.countByUser(user);

        if (totalPredictions == 0) return 0.0;

        // Each prediction can give max 5 points
        double maxPossiblePoints = totalPredictions * 5.0;

        return (totalPoints / maxPossiblePoints) * 100;
    }





    @PostConstruct
    public void init() {
        int season = 2025;

        updatePointsForFinishedMatches();
        updateSeasonLeaderboard();
        updateAllTimeLeaderboard();

        System.out.println("Team matches at startup!");
    }


    // exposed
    // public List<ScorePrediction> getAllPredictions() {
    //     return scorePredictionRepository.findAll();
    // }

    // public List<ScorePrediction> getPredictionsByTeam(String teamName) {
    //     return scorePredictionRepository.findByMatch_HomeTeamOrMatch_AwayTeam(teamName, teamName);
    // }

    // public List<ScorePrediction> getPredictionsBySeason(Integer season) {
    //     return scorePredictionRepository.findBySeason(season);
    // }

    // public List<ScorePrediction> getPredictionsByMatch(Long matchId) {
    //     return scorePredictionRepository.findByMatch_MatchId(matchId);
    // }

    // Helper method to map entity to DTO
    private ExposedApiDTO mapToDTO(ScorePrediction prediction) {
        var match = prediction.getMatch();
        return new ExposedApiDTO(
            match.getMatchId(),
            match.getHomeTeam(),
            match.getAwayTeam(),
            match.getMatchStatus(),
            prediction.getSeason(),
            match.getLeagueRound(),
            match.getDateTime(),
            match.getVenue(),
            match.getHomeLogo(),
            match.getAwayLogo(),
            prediction.getPredHomeScore(),
            prediction.getPredAwayScore(),
            match.getHomeScore(),
            match.getAwayScore()
        );
    }

    public List<ExposedApiDTO> getAllPredictions() {
        return scorePredictionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ExposedApiDTO> getPredictionsByTeam(String teamName) {
        return scorePredictionRepository.findByMatch_HomeTeamOrMatch_AwayTeam(teamName, teamName)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ExposedApiDTO> getPredictionsBySeason(Integer season) {
        return scorePredictionRepository.findBySeason(season)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ExposedApiDTO> getPredictionsByMatch(Long matchId) {
        return scorePredictionRepository.findByMatch_MatchId(matchId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



}
