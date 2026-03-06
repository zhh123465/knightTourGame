package com.knighttour.util;

import com.knighttour.exception.ValidationException;
import com.knighttour.model.Position;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidatorPropertyTest {

    @Property
    void validPositionInput(@ForAll @IntRange(min = 0, max = 7) int row, 
                            @ForAll @IntRange(min = 0, max = 7) int col) throws ValidationException {
        String input = row + "," + col;
        Position pos = Validator.parsePosition(input);
        
        assertThat(pos.getRow()).isEqualTo(row);
        assertThat(pos.getCol()).isEqualTo(col);
    }

    @Property
    void invalidPositionInput(@ForAll("invalidInputs") String input) {
        assertThatThrownBy(() -> Validator.parsePosition(input))
            .isInstanceOf(ValidationException.class);
    }
    
    @Provide
    Arbitrary<String> invalidInputs() {
        return Arbitraries.oneOf(
            Arbitraries.strings().alpha().ofMinLength(1), // 非数字
            Arbitraries.strings().numeric().ofLength(1),  // 缺少逗号
            Arbitraries.integers().filter(i -> i < 0 || i > 7).map(i -> i + ",0"), // 行无效
            Arbitraries.integers().filter(i -> i < 0 || i > 7).map(i -> "0," + i)  // 列无效
        );
    }
}
