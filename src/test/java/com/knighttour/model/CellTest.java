package com.knighttour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testConstructorAndGetters() {
        Cell cell = new Cell(2, 5);
        assertEquals(2, cell.getRow());
        assertEquals(5, cell.getCol());
        assertFalse(cell.isVisited());
        assertEquals(0, cell.getSequence());
    }

    @Test
    void testSetters() {
        Cell cell = new Cell(1, 1);
        
        cell.setVisited(true);
        assertTrue(cell.isVisited());
        
        cell.setSequence(10);
        assertEquals(10, cell.getSequence());
    }

    @Test
    void testEqualsAndHashCode() {
        Cell c1 = new Cell(1, 2);
        Cell c2 = new Cell(1, 2);
        Cell c3 = new Cell(2, 1);
        
        // equals should only check coordinates
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode());
        
        // modifying state should not affect equality (if based on coordinates)
        c2.setVisited(true);
        assertEquals(c1, c2);
    }

    @Test
    void testToString() {
        Cell cell = new Cell(0, 0);
        assertTrue(cell.toString().contains("Cell(0,0"));
    }
}
