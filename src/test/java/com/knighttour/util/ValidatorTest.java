package com.knighttour.util;

import com.knighttour.exception.ValidationException;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void testIsValidPosition() {
        assertTrue(Validator.isValidPosition(0, 0));
        assertTrue(Validator.isValidPosition(7, 7));
        assertFalse(Validator.isValidPosition(-1, 0));
        assertFalse(Validator.isValidPosition(0, 8));
    }

    @Test
    void testParsePosition() throws ValidationException {
        Position pos = Validator.parsePosition("3, 4");
        assertEquals(3, pos.getRow());
        assertEquals(4, pos.getCol());
        
        pos = Validator.parsePosition(" 0 , 7 ");
        assertEquals(0, pos.getRow());
        assertEquals(7, pos.getCol());
    }

    @Test
    void testParsePositionInvalidFormat() {
        assertThrows(ValidationException.class, () -> Validator.parsePosition("invalid"));
        assertThrows(ValidationException.class, () -> Validator.parsePosition("1,2,3"));
        assertThrows(ValidationException.class, () -> Validator.parsePosition(""));
    }

    @Test
    void testParsePositionInvalidCoordinates() {
        assertThrows(ValidationException.class, () -> Validator.parsePosition("-1, 0"));
        assertThrows(ValidationException.class, () -> Validator.parsePosition("0, 8"));
        assertThrows(ValidationException.class, () -> Validator.parsePosition("a, b"));
    }

    @Test
    void testValidateAndPlace() throws ValidationException {
        Board board = new Board();
        Position pos = new Position(3, 3);
        
        assertDoesNotThrow(() -> Validator.validateAndPlace(board, pos));
        
        board.markVisited(3, 3, 1);
        assertThrows(ValidationException.class, () -> Validator.validateAndPlace(board, pos));
        
        assertThrows(ValidationException.class, () -> Validator.validateAndPlace(board, new Position(8, 8)));
    }
}
