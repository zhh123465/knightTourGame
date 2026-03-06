package com.knighttour.model;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PositionPropertyTest {

    @Property
    void validPositionCheck(@ForAll @IntRange(min = 0, max = 7) int row, 
                            @ForAll @IntRange(min = 0, max = 7) int col) {
        Position pos = new Position(row, col);
        assertThat(pos.isValid()).isTrue();
    }

    @Property
    void invalidPositionCheck(@ForAll("invalidRows") int row, 
                              @ForAll("invalidCols") int col) {
        Position pos = new Position(row, col);
        assertThat(pos.isValid()).isFalse();
    }
    
    @Provide
    Arbitrary<Integer> invalidRows() {
        return Arbitraries.integers().filter(i -> i < 0 || i > 7);
    }
    
    @Provide
    Arbitrary<Integer> invalidCols() {
        return Arbitraries.integers().filter(i -> i < 0 || i > 7);
    }
    
    @Property
    void distanceSymmetry(@ForAll @IntRange(min = 0, max = 7) int r1, 
                          @ForAll @IntRange(min = 0, max = 7) int c1,
                          @ForAll @IntRange(min = 0, max = 7) int r2, 
                          @ForAll @IntRange(min = 0, max = 7) int c2) {
        Position p1 = new Position(r1, c1);
        Position p2 = new Position(r2, c2);
        
        assertThat(p1.distanceTo(p2)).isEqualTo(p2.distanceTo(p1));
    }
    
    @Property
    void distanceNonNegative(@ForAll @IntRange(min = 0, max = 7) int r1, 
                             @ForAll @IntRange(min = 0, max = 7) int c1,
                             @ForAll @IntRange(min = 0, max = 7) int r2, 
                             @ForAll @IntRange(min = 0, max = 7) int c2) {
        Position p1 = new Position(r1, c1);
        Position p2 = new Position(r2, c2);
        
        assertThat(p1.distanceTo(p2)).isGreaterThanOrEqualTo(0.0);
    }
}
