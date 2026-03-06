package com.knighttour.algorithm;

import com.knighttour.model.Move;
import com.knighttour.model.Position;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveStackPropertyTest {

    @Property
    void stackOperations(@ForAll @IntRange(min = 1, max = 64) int count) {
        MoveStack stack = new MoveStack();
        
        for (int i = 0; i < count; i++) {
            Move move = new Move(new Position(0, 0), new Position(1, 2), i, Collections.emptyList());
            stack.push(move);
        }
        
        assertThat(stack.size()).isEqualTo(count);
        assertThat(stack.isEmpty()).isFalse();
        
        for (int i = 0; i < count; i++) {
            stack.pop();
        }
        
        assertThat(stack.isEmpty()).isTrue();
    }
}
