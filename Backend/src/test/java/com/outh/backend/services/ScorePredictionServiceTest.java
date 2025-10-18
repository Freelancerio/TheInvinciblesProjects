package com.outh.backend.services;

import com.outh.backend.dto.*;
import com.outh.backend.models.*;
import com.outh.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScorePredictionServiceTest {

    @Mock
    private ScorePredictionRepository scorePredictionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeagueMatchesRepository matchRepository;

    @Mock
    private SeasonLeaderboardRepository leaderboardRepository;

    @Mock
    private AllTimeLeaderboardRepository allTimeLeaderboardRepository;

    @InjectMocks
    private ScorePredictionService scorePredictionService;

    private User testUser;
    private LeagueMatches testMatch;
    private ScorePrediction testPrediction;
    private ScorePredictionRequest predictionRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirebaseId("user123");
        testUser.setUsername("testuser");

        testMatch = new LeagueMatches();
        testMatch.setMatchId(1L);
        testMatch.setHomeTeam("Arsenal");
        testMatch.setAwayTeam("Chelsea");
        testMatch.setMatchStatus("NS");
        testMatch.setSeason(2024);
        testMatch.setHomeScore(null);
        testMatch.setAwayScore(null);

        testPrediction = new ScorePrediction();
        testPrediction.setId(1L);
        testPrediction.setUser(testUser);
        testPrediction.setMatch(testMatch);
        testPrediction.setSeason(2024);
        testPrediction.setPredHomeScore(2);
        testPrediction.setPredAwayScore(1);
        testPrediction.setPoints(null);

        predictionRequest = new ScorePredictionRequest();
        predictionRequest.setFirebaseId("user123");
        predictionRequest.setMatchId(1L);
        predictionRequest.setSeason(2024);
        predictionRequest.setPredHomeScore(2);
        predictionRequest.setPredAwayScore(1);
    }

    @Test
    void savePrediction_Success() {
        // Arrange
        when(userRepository.findByFirebaseId("user123")).thenReturn(Optional.of(testUser));
        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(scorePredictionRepository.findByUserAndMatch(testUser, testMatch))
                .thenReturn(Optional.empty());
        when(scorePredictionRepository.save(any(ScorePrediction.class))).thenReturn(testPrediction);

        // Act
        ScorePrediction result = scorePredictionService.savePrediction(predictionRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPredHomeScore());
        assertEquals(1, result.getPredAwayScore());
        verify(scorePredictionRepository, times(1)).save(any(ScorePrediction.class));
    }

    @Test
    void savePrediction_UserNotFound() {
        // Arrange
        when(userRepository.findByFirebaseId("user123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            scorePredictionService.savePrediction(predictionRequest);
        });
    }

    @Test
    void savePrediction_MatchNotFound() {
        // Arrange
        when(userRepository.findByFirebaseId("user123")).thenReturn(Optional.of(testUser));
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            scorePredictionService.savePrediction(predictionRequest);
        });
    }

    @Test
    void savePrediction_DuplicatePrediction() {
        // Arrange
        when(userRepository.findByFirebaseId("user123")).thenReturn(Optional.of(testUser));
        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(scorePredictionRepository.findByUserAndMatch(testUser, testMatch))
                .thenReturn(Optional.of(testPrediction));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            scorePredictionService.savePrediction(predictionRequest);
        });
    }

    @Test
    void updatePointsForFinishedMatches_ExactScore() {
        // Arrange
        testMatch.setMatchStatus("FT");
        testMatch.setHomeScore(2);
        testMatch.setAwayScore(1);
        testPrediction.setMatch(testMatch);

        List<ScorePrediction> predictions = Arrays.asList(testPrediction);
        when(scorePredictionRepository.findAll()).thenReturn(predictions);
        when(scorePredictionRepository.save(any(ScorePrediction.class))).thenReturn(testPrediction);

        // Act
        scorePredictionService.updatePointsForFinishedMatches();

        // Assert
        assertEquals(5, testPrediction.getPoints());
        verify(scorePredictionRepository, times(1)).save(testPrediction);
    }

    @Test
    void updatePointsForFinishedMatches_CorrectOutcome() {
        // Arrange
        testMatch.setMatchStatus("FT");
        testMatch.setHomeScore(3);
        testMatch.setAwayScore(1); // Different score but same outcome (home win)
        testPrediction.setMatch(testMatch);

        List<ScorePrediction> predictions = Arrays.asList(testPrediction);
        when(scorePredictionRepository.findAll()).thenReturn(predictions);
        when(scorePredictionRepository.save(any(ScorePrediction.class))).thenReturn(testPrediction);

        // Act
        scorePredictionService.updatePointsForFinishedMatches();

        // Assert
        assertEquals(2, testPrediction.getPoints()); // 2 points for correct outcome
        verify(scorePredictionRepository, times(1)).save(testPrediction);
    }



    @Test
    void updatePointsForFinishedMatches_PenaltyForLargeDifference() {
        // Arrange
        testMatch.setMatchStatus("FT");
        testMatch.setHomeScore(4); // Predicted 2, actual 4 -> difference of 2
        testMatch.setAwayScore(1);
        testPrediction.setMatch(testMatch);

        List<ScorePrediction> predictions = Arrays.asList(testPrediction);
        when(scorePredictionRepository.findAll()).thenReturn(predictions);
        when(scorePredictionRepository.save(any(ScorePrediction.class))).thenReturn(testPrediction);

        // Act
        scorePredictionService.updatePointsForFinishedMatches();

        // Assert
        assertEquals(1, testPrediction.getPoints()); // 2 points - 1 penalty = 1
        verify(scorePredictionRepository, times(1)).save(testPrediction);
    }

    @Test
    void getSeasonLeaderboard_Success() {
        // Arrange
        SeasonLeaderboard leaderboardEntry = new SeasonLeaderboard();
        leaderboardEntry.setUser(testUser);
        leaderboardEntry.setSeason(2024);
        leaderboardEntry.setPoints(15);

        List<SeasonLeaderboard> leaderboard = Arrays.asList(leaderboardEntry);
        when(leaderboardRepository.findBySeasonOrderByPointsDesc(2024)).thenReturn(leaderboard);

        // Act
        List<LeaderboardEntryDTO> result = scorePredictionService.getSeasonLeaderboard(2024);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals(15, result.get(0).getPoints());
        assertEquals(2024, result.get(0).getSeason());
        verify(leaderboardRepository, times(1)).findBySeasonOrderByPointsDesc(2024);
    }

    @Test
    void getAllTimeLeaderboard_Success() {
        // Arrange
        AllTimeLeaderboard allTimeEntry = new AllTimeLeaderboard();
        allTimeEntry.setUser(testUser);
        allTimeEntry.setPoints(50);

        List<AllTimeLeaderboard> allTimeLeaderboard = Arrays.asList(allTimeEntry);
        when(allTimeLeaderboardRepository.findAllByOrderByPointsDesc()).thenReturn(allTimeLeaderboard);

        // Act
        List<LeaderboardEntryDTO> result = scorePredictionService.getAllTimeLeaderboard();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals(50, result.get(0).getPoints());
        assertNull(result.get(0).getSeason());
        verify(allTimeLeaderboardRepository, times(1)).findAllByOrderByPointsDesc();
    }

    @Test
    void getUserAllTimePosition_Success() {
        // Arrange
        AllTimeLeaderboard entry1 = new AllTimeLeaderboard();
        User user1 = new User();
        user1.setFirebaseId("user1");
        entry1.setUser(user1);
        entry1.setPoints(100);

        AllTimeLeaderboard entry2 = new AllTimeLeaderboard();
        entry2.setUser(testUser);
        entry2.setPoints(50);

        List<AllTimeLeaderboard> leaderboard = Arrays.asList(entry1, entry2);
        when(allTimeLeaderboardRepository.findAllByOrderByPointsDesc()).thenReturn(leaderboard);

        // Act
        int position = scorePredictionService.getUserAllTimePosition("user123");

        // Assert
        assertEquals(2, position);
        verify(allTimeLeaderboardRepository, times(1)).findAllByOrderByPointsDesc();
    }

    @Test
    void getUserAllTimeAccuracy_Success() {
        // Arrange
        when(userRepository.findByFirebaseId("user123")).thenReturn(Optional.of(testUser));

        AllTimeLeaderboard allTimeEntry = new AllTimeLeaderboard();
        allTimeEntry.setUser(testUser);
        allTimeEntry.setPoints(15);
        when(allTimeLeaderboardRepository.findByUser_FirebaseId("user123"))
                .thenReturn(Optional.of(allTimeEntry));

        when(scorePredictionRepository.countByUser(testUser)).thenReturn(5L);

        // Act
        double accuracy = scorePredictionService.getUserAllTimeAccuracy("user123");

        // Assert
        assertEquals(60.0, accuracy); // 15 points / (5 * 5 max points) * 100 = 60%
        verify(userRepository, times(1)).findByFirebaseId("user123");
        verify(allTimeLeaderboardRepository, times(1)).findByUser_FirebaseId("user123");
        verify(scorePredictionRepository, times(1)).countByUser(testUser);
    }

    @Test
    void getUserAllTimeAccuracy_NoPredictions() {
        // Arrange
        when(userRepository.findByFirebaseId("user123")).thenReturn(Optional.of(testUser));

        AllTimeLeaderboard allTimeEntry = new AllTimeLeaderboard();
        allTimeEntry.setUser(testUser);
        allTimeEntry.setPoints(0);
        when(allTimeLeaderboardRepository.findByUser_FirebaseId("user123"))
                .thenReturn(Optional.of(allTimeEntry));

        when(scorePredictionRepository.countByUser(testUser)).thenReturn(0L);

        // Act
        double accuracy = scorePredictionService.getUserAllTimeAccuracy("user123");

        // Assert
        assertEquals(0.0, accuracy);
    }

    @Test
    void getAllPredictions_Success() {
        // Arrange
        when(scorePredictionRepository.findAll()).thenReturn(Arrays.asList(testPrediction));

        // Act
        List<ExposedApiDTO> result = scorePredictionService.getAllPredictions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scorePredictionRepository, times(1)).findAll();
    }

    @Test
    void getPredictionsByTeam_Success() {
        // Arrange
        when(scorePredictionRepository.findByMatch_HomeTeamOrMatch_AwayTeam("Arsenal", "Arsenal"))
                .thenReturn(Arrays.asList(testPrediction));

        // Act
        List<ExposedApiDTO> result = scorePredictionService.getPredictionsByTeam("Arsenal");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scorePredictionRepository, times(1))
                .findByMatch_HomeTeamOrMatch_AwayTeam("Arsenal", "Arsenal");
    }

    @Test
    void calculatePoints_ExactScore() {
        // Act
        int points = scorePredictionService.calculatePoints(2, 1, 2, 1);

        // Assert
        assertEquals(5, points);
    }

    @Test
    void calculatePoints_CorrectOutcome() {
        // Act
        int points = scorePredictionService.calculatePoints(1, 0, 2, 1); // Both home wins

        // Assert
        assertEquals(2, points);
    }


    @Test
    void calculatePoints_PenaltyApplied() {
        // Act
        int points = scorePredictionService.calculatePoints(1, 0, 3, 0); // Large difference

        // Assert
        assertEquals(1, points); // 2 points - 1 penalty
    }
}