package com.knighttour.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    @Test
    void testValidSolution() {
        int[][] matrix = new int[8][8];
        int count = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrix[i][j] = count++;
            }
        }
        
        Solution solution = new Solution(matrix, new ArrayList<>(), 100, 64, 0);
        assertTrue(solution.isValid());
    }

    @Test
    void testInvalidSolutionDuplicate() {
        int[][] matrix = new int[8][8];
        // Fill with valid numbers first
        int count = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrix[i][j] = count++;
            }
        }
        // Introduce duplicate
        matrix[7][7] = 1; 
        
        Solution solution = new Solution(matrix, Collections.emptyList(), 0, 0, 0);
        assertFalse(solution.isValid());
    }

    @Test
    void testInvalidSolutionOutOfRange() {
        int[][] matrix = new int[8][8];
        // Fill with valid numbers first
        int count = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrix[i][j] = count++;
            }
        }
        // Introduce invalid number
        matrix[0][0] = 65;
        
        Solution solution = new Solution(matrix, Collections.emptyList(), 0, 0, 0);
        assertFalse(solution.isValid());
    }

    @Test
    void testToFormattedString() {
        int[][] matrix = new int[8][8];
        Solution solution = new Solution(matrix, Collections.emptyList(), 0, 0, 0);
        String output = solution.toFormattedString();
        assertNotNull(output);
        assertTrue(output.contains("  0")); // Should contain formatted zeros
    }
    
    @Test
    void testGettersAndImmutability() {
        int[][] matrix = new int[8][8];
        matrix[0][0] = 1;
        
        Solution solution = new Solution(matrix, new ArrayList<>(), 100, 10, 5);
        
        assertEquals(100, solution.getSolvingTimeMs());
        assertEquals(10, solution.getTotalMoves());
        assertEquals(5, solution.getBacktrackCount());
        
        // Modify original matrix, solution should not change (deep copy in constructor)
        matrix[0][0] = 99;
        assertEquals(1, solution.getMatrix()[0][0]);
        
        // Modify returned matrix, solution should not change (defensive copy in getter)
        int[][] retrievedMatrix = solution.getMatrix();
        retrievedMatrix[0][0] = 88;
        assertEquals(1, solution.getMatrix()[0][0]);
    }
}
