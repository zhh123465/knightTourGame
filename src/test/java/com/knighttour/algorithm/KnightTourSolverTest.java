package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KnightTourSolverTest {

    @Test
    void testSolveFromCorner() {
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        
        // Solving from (0,0)
        Solution solution = solver.solve(new Position(0, 0));
        
        // For a perfect tour without backtracking, moves should be 63.
        // But totalMoves includes backtracking steps, so it could be >= 63.
        // Since we use heuristics, it's likely 63 or slightly more.
        // Let's just verify it found a complete path.
        assertTrue(solution.getTotalMoves() >= 63, "Should have at least 63 moves");
        
        // Path size (List<Move>) should be 63 for a complete tour
        assertEquals(63, solution.getPath().size(), "Path should have 63 moves (edges)");
        
        // Visited count on board should be 64
        assertEquals(64, board.getVisitedCount());
    }

    @Test
    void testSolveFromCenter() {
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        
        Solution solution = solver.solve(new Position(3, 3));
        
        assertNotNull(solution, "Should find a solution from (3,3)");
        assertTrue(solution.isValid());
    }
    
    @Test
    void testSolverState() {
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        
        assertEquals(SolverState.IDLE, solver.getState());
        
        solver.solve(new Position(0, 0));
        
        assertEquals(SolverState.SOLUTION_FOUND, solver.getState());
    }
    
    // Note: Testing pause/resume/stop requires running solve in a separate thread,
    // which is complex for a unit test. Integration tests are better for that.
}
