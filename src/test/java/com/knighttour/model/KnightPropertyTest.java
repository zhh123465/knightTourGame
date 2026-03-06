package com.knighttour.model;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import static org.assertj.core.api.Assertions.assertThat;

public class KnightPropertyTest {

    @Property
    void knightInitialization(@ForAll @IntRange(min = 0, max = 7) int row, 
                              @ForAll @IntRange(min = 0, max = 7) int col) {
        Knight knight = new Knight(row, col);
        
        assertThat(knight.getCurrentPosition().getRow()).isEqualTo(row);
        assertThat(knight.getCurrentPosition().getCol()).isEqualTo(col);
        assertThat(knight.getMoveCount()).isZero();
    }
    
    @Property
    void moveCountCorrectness(@ForAll @IntRange(min = 1, max = 64) int moves) {
        Knight knight = new Knight(0, 0);
        for (int i = 0; i < moves; i++) {
            knight.incrementMoveCount();
        }
        
        assertThat(knight.getMoveCount()).isEqualTo(moves);
        
        for (int i = 0; i < moves; i++) {
            knight.decrementMoveCount();
        }
        
        assertThat(knight.getMoveCount()).isZero();
    }
}
