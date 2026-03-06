package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {

    @Test
    void testGetValidMovesFromCenter() {
        Board board = new Board();
        MoveGenerator generator = new MoveGenerator(board);
        
        // (4,4) is in the center, should have 8 moves
        List<Position> moves = generator.getValidMoves(new Position(4, 4));
        assertEquals(8, moves.size());
    }

    @Test
    void testGetValidMovesFromCorner() {
        Board board = new Board();
        MoveGenerator generator = new MoveGenerator(board);
        
        // (0,0) is a corner, should have 2 moves: (1,2) and (2,1)
        List<Position> moves = generator.getValidMoves(new Position(0, 0));
        assertEquals(2, moves.size());
        assertTrue(moves.contains(new Position(1, 2)));
        assertTrue(moves.contains(new Position(2, 1)));
    }

    @Test
    void testExcludeVisitedCells() {
        Board board = new Board();
        MoveGenerator generator = new MoveGenerator(board);
        
        // Mark (1,2) as visited
        board.markVisited(1, 2, 1);
        
        // (0,0) usually can go to (1,2) and (2,1). Now only (2,1) should be available.
        List<Position> moves = generator.getValidMoves(new Position(0, 0));
        assertEquals(1, moves.size());
        assertEquals(new Position(2, 1), moves.get(0));
    }

    @Test
    void testHeuristicSorting() {
        Board board = new Board();
        MoveGenerator generator = new MoveGenerator(board);
        
        // From (0,0), moves are (1,2) and (2,1).
        // (1,2) has moves: (0,0)[visited], (2,0), (3,1), (3,3), (2,4), (0,4) -> 5 moves (excluding start)
        // (2,1) has moves: (0,0)[visited], (0,2), (1,3), (3,3), (4,2), (4,0) -> 5 moves
        // Let's pick a position where counts differ.
        // (3,3) center-ish. Moves:
        // (1,2) -> corner-ish (fewer moves)
        // (5,4) -> center-ish (more moves)
        
        List<Position> moves = generator.getValidMovesWithHeuristic(new Position(3, 3));
        
        // Check if sorted by available moves count (ascending)
        int prevCount = -1;
        for (Position move : moves) {
            int count = generator.countValidMoves(move);
            if (prevCount != -1) {
                assertTrue(count >= prevCount, "Moves should be sorted by heuristic score");
            }
            prevCount = count;
        }
    }
}
