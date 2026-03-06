package com.knighttour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testInitialization() {
        Board board = new Board();
        assertEquals(8, board.getSize());
        assertEquals(0, board.getVisitedCount());

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertFalse(board.isVisited(i, j));
                assertEquals(0, board.getCell(i, j).getSequence());
            }
        }
    }

    @Test
    void testMarkVisited() {
        Board board = new Board();
        board.markVisited(3, 3, 1);

        assertTrue(board.isVisited(3, 3));
        assertEquals(1, board.getCell(3, 3).getSequence());
        assertEquals(1, board.getVisitedCount());
    }

    @Test
    void testMarkVisitedDuplicate() {
        Board board = new Board();
        board.markVisited(0, 0, 1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            board.markVisited(0, 0, 2);
        });
    }

    @Test
    void testMarkUnvisited() {
        Board board = new Board();
        board.markVisited(4, 4, 1);
        assertEquals(1, board.getVisitedCount());
        
        board.markUnvisited(4, 4);
        assertFalse(board.isVisited(4, 4));
        assertEquals(0, board.getCell(4, 4).getSequence());
        assertEquals(0, board.getVisitedCount());
    }

    @Test
    void testMarkUnvisitedOnUnvisitedCell() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, () -> {
            board.markUnvisited(0, 0);
        });
    }

    @Test
    void testReset() {
        Board board = new Board();
        board.markVisited(0, 0, 1);
        board.markVisited(1, 2, 2);
        
        board.reset();
        
        assertEquals(0, board.getVisitedCount());
        assertFalse(board.isVisited(0, 0));
        assertFalse(board.isVisited(1, 2));
    }

    @Test
    void testGetSolutionMatrix() {
        Board board = new Board();
        board.markVisited(0, 0, 1);
        board.markVisited(0, 1, 2);
        
        int[][] matrix = board.getSolutionMatrix();
        assertEquals(1, matrix[0][0]);
        assertEquals(2, matrix[0][1]);
        assertEquals(0, matrix[0][2]);
    }
    
    @Test
    void testInvalidPosition() {
        Board board = new Board();
        assertFalse(board.isValidPosition(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> board.getCell(-1, 0));
    }
}
