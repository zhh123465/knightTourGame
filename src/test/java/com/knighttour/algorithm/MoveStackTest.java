package com.knighttour.algorithm;

import com.knighttour.model.Move;
import com.knighttour.model.Position;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MoveStackTest {

    @Test
    void testPushAndPop() {
        MoveStack stack = new MoveStack();
        Move move = new Move(new Position(0, 0), new Position(1, 2), 1);
        
        stack.push(move);
        assertEquals(1, stack.size());
        assertFalse(stack.isEmpty());
        
        Move popped = stack.pop();
        assertEquals(move, popped);
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPeek() {
        MoveStack stack = new MoveStack();
        Move move = new Move(new Position(0, 0), new Position(1, 2), 1);
        
        stack.push(move);
        assertEquals(move, stack.peek());
        assertEquals(1, stack.size()); // Size shouldn't change
    }

    @Test
    void testIsFull() {
        MoveStack stack = new MoveStack(2); // Small size for testing
        assertFalse(stack.isFull());
        
        stack.push(new Move(new Position(0, 0), new Position(1, 2), 1));
        assertFalse(stack.isFull());
        
        stack.push(new Move(new Position(1, 2), new Position(2, 4), 2));
        assertTrue(stack.isFull());
        
        assertThrows(IllegalStateException.class, () -> {
            stack.push(new Move(new Position(2, 4), new Position(3, 6), 3));
        });
    }

    @Test
    void testClear() {
        MoveStack stack = new MoveStack();
        stack.push(new Move(new Position(0, 0), new Position(1, 2), 1));
        stack.clear();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }
    
    @Test
    void testGetPath() {
        MoveStack stack = new MoveStack();
        Move m1 = new Move(new Position(0, 0), new Position(1, 2), 1);
        Move m2 = new Move(new Position(1, 2), new Position(2, 4), 2);
        
        stack.push(m1);
        stack.push(m2);
        
        List<Move> path = stack.getPath();
        assertEquals(2, path.size());
        assertEquals(m1, path.get(0));
        assertEquals(m2, path.get(1));
    }
}
