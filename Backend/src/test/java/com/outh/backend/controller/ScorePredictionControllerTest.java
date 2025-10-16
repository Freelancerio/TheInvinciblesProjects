package com.outh.backend.controller;

import com.outh.backend.dto.ScorePredictionRequest;
import com.outh.backend.models.ScorePrediction;
import com.outh.backend.services.ScorePredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScorePredictionControllerTest {

    @Mock
    private ScorePredictionService scorePredictionService;

    @InjectMocks
    private ScorePredictionController scorePredictionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPrediction_ShouldReturnOkResponse_WhenSuccessful() {
        // given
        ScorePredictionRequest request = new ScorePredictionRequest();
        ScorePrediction prediction = new ScorePrediction();
        when(scorePredictionService.savePrediction(request)).thenReturn(prediction);

        // when
        ResponseEntity<?> response = scorePredictionController.createPrediction(request);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(prediction, response.getBody());
        verify(scorePredictionService, times(1)).savePrediction(request);
    }

    @Test
    void createPrediction_ShouldReturnBadRequest_WhenExceptionThrown() {
        // given
        ScorePredictionRequest request = new ScorePredictionRequest();
        when(scorePredictionService.savePrediction(request))
                .thenThrow(new RuntimeException("Invalid data"));

        // when
        ResponseEntity<?> response = scorePredictionController.createPrediction(request);

        // then
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid data", response.getBody());
        verify(scorePredictionService, times(1)).savePrediction(request);
    }
}
