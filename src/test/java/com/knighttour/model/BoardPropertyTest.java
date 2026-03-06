package com.knighttour.model;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardPropertyTest {

    @Property
    void boardInitializationCompleteness() {
        Board board = new Board();
        
        // 验证所有单元格都在有效范围内
        assertThat(board.getVisitedCount()).isZero();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertThat(board.isValidPosition(row, col)).isTrue();
                assertThat(board.isVisited(row, col)).isFalse();
                assertThat(board.getCell(row, col).getSequence()).isZero();
            }
        }
    }

    @Property
    void markVisitedCorrectness(@ForAll @IntRange(min = 0, max = 7) int row, 
                                @ForAll @IntRange(min = 0, max = 7) int col,
                                @ForAll @IntRange(min = 1, max = 64) int sequence) {
        Board board = new Board();
        board.markVisited(row, col, sequence);
        
        assertThat(board.isVisited(row, col)).isTrue();
        assertThat(board.getCell(row, col).getSequence()).isEqualTo(sequence);
        assertThat(board.getVisitedCount()).isEqualTo(1);
    }
    
    @Property
    void markUnvisitedCorrectness(@ForAll @IntRange(min = 0, max = 7) int row, 
                                  @ForAll @IntRange(min = 0, max = 7) int col) {
        Board board = new Board();
        board.markVisited(row, col, 1);
        board.markUnvisited(row, col);
        
        assertThat(board.isVisited(row, col)).isFalse();
        assertThat(board.getCell(row, col).getSequence()).isZero();
        assertThat(board.getVisitedCount()).isZero();
    }
    
    @Property
    void resetCompleteness(@ForAll @IntRange(min = 0, max = 7) int row, 
                           @ForAll @IntRange(min = 0, max = 7) int col) {
        Board board = new Board();
        board.markVisited(row, col, 1);
        board.reset();
        
        assertThat(board.getVisitedCount()).isZero();
        assertThat(board.isVisited(row, col)).isFalse();
    }
}
