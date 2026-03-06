package com.knighttour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testConstructorAndGetters() {
        Position pos = new Position(3, 4);
        assertEquals(3, pos.getRow());
        assertEquals(4, pos.getCol());
    }

    @Test
    void testIsValid() {
        assertTrue(new Position(0, 0).isValid());
        assertTrue(new Position(7, 7).isValid());
        assertTrue(new Position(3, 4).isValid());

        assertFalse(new Position(-1, 0).isValid());
        assertFalse(new Position(0, -1).isValid());
        assertFalse(new Position(8, 0).isValid());
        assertFalse(new Position(0, 8).isValid());
    }

    @Test
    void testDistanceTo() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 4);
        
        // 3^2 + 4^2 = 9 + 16 = 25, sqrt(25) = 5
        assertEquals(5.0, p1.distanceTo(p2), 0.0001);
        
        Position p3 = new Position(1, 1);
        assertEquals(0.0, p1.distanceTo(p1), 0.0001);
    }

    @Test
    void testEqualsAndHashCode() {
        Position p1 = new Position(2, 3);
        Position p2 = new Position(2, 3);
        Position p3 = new Position(3, 2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    void testToString() {
        Position pos = new Position(5, 6);
        assertEquals("(5, 6)", pos.toString());
    }
}
