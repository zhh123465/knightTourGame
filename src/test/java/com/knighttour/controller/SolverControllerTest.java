package com.knighttour.controller;

import com.knighttour.algorithm.SolverState;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.view.BoardView;
import com.knighttour.view.KnightAnimator;
import com.knighttour.view.StatisticsPanel;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * SolverController 单元测试
 */
@ExtendWith(MockitoExtension.class)
public class SolverControllerTest {

    @Mock
    private BoardView boardView;

    @Mock
    private StatisticsPanel statisticsPanel;

    @Mock
    private KnightAnimator animator;

    private Board board;
    private SolverController controller;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit for Platform.runLater
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await(5, TimeUnit.SECONDS);
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this); // Handled by @ExtendWith(MockitoExtension.class)
        board = new Board();
        controller = new SolverController(board, boardView, statisticsPanel, animator);
    }

    @Test
    void testInitialState() {
        assertEquals(SolverState.IDLE, controller.getState());
    }

    @Test
    void testSolve() throws InterruptedException {
        Position startPos = new Position(0, 0);
        
        // When
        controller.solve(startPos, new com.knighttour.algorithm.WarnsdorffAlgorithm());
        
        // Allow some time for the thread to start
        Thread.sleep(100);
        
        // Then
        // Since solve runs in a separate thread, state might be SOLVING or even finished if fast enough
        // But for 8x8 it takes some time, so it should be SOLVING
        // However, Mockito verification is safer
        verify(statisticsPanel, atLeastOnce()).reset();
        verify(boardView, atLeastOnce()).resetBoard();
        verify(boardView, atLeastOnce()).placeKnight(any(Position.class));
    }

    @Test
    void testPauseAndResume() {
        // Given
        // We need to be in SOLVING state to pause. 
        // Mocking the internal solver state is hard without reflection or changing design.
        // But we can test that calling pause/resume doesn't crash
        
        controller.pause();
        // State shouldn't change if it was IDLE
        assertEquals(SolverState.IDLE, controller.getState());
        
        controller.resume();
        assertEquals(SolverState.IDLE, controller.getState());
    }
    
    @Test
    void testStop() {
        controller.stop();
        assertEquals(SolverState.IDLE, controller.getState());
    }
}
