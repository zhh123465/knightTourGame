package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class MoveGeneratorPropertyTest {

    @Property
    void validMoveCount(@ForAll @IntRange(min = 0, max = 7) int row, 
                        @ForAll @IntRange(min = 0, max = 7) int col) {
        Board board = new Board();
        MoveGenerator generator = new MoveGenerator(board);
        Position pos = new Position(row, col);
        
        List<Position> moves = generator.getValidMoves(pos);
        
        // 一个骑士最多有8个移动，最少2个（角落）
        assertThat(moves.size()).isBetween(2, 8);
        
        // 验证每个移动都在棋盘内
        for (Position p : moves) {
            assertThat(board.isValidPosition(p.getRow(), p.getCol())).isTrue();
            // 验证移动距离是根号5
            double dist = pos.distanceTo(p);
            assertThat(dist).isCloseTo(Math.sqrt(5), offset(0.0001));
        }
    }

    @Property
    void heuristicSortOrder(@ForAll @IntRange(min = 0, max = 7) int row, 
                            @ForAll @IntRange(min = 0, max = 7) int col) {
        Board board = new Board();
        MoveGenerator generator = new MoveGenerator(board);
        Position pos = new Position(row, col);
        
        List<Position> moves = generator.getValidMovesWithHeuristic(pos);
        
        // 验证排序是否按照可行移动数升序排列
        for (int i = 0; i < moves.size() - 1; i++) {
            int count1 = generator.countValidMoves(moves.get(i));
            int count2 = generator.countValidMoves(moves.get(i+1));
            assertThat(count1).isLessThanOrEqualTo(count2);
        }
    }
}
