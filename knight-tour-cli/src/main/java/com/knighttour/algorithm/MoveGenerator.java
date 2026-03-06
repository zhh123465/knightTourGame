package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 移动生成器，计算骑士的所有合法移动
 * 
 * @author KnightTour
 * @version 1.0
 */
public class MoveGenerator {
    private static final int[][] MOVE_OFFSETS = {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    private final Board board;

    /**
     * 构造函数
     * 
     * @param board 棋盘对象
     */
    public MoveGenerator(Board board) {
        this.board = board;
    }

    /**
     * 获取指定位置的所有合法移动
     * 
     * @param from 起始位置
     * @return 合法移动列表
     */
    public List<Position> getValidMoves(Position from) {
        List<Position> validMoves = new ArrayList<>();
        
        for (int[] offset : MOVE_OFFSETS) {
            int newRow = from.getRow() + offset[0];
            int newCol = from.getCol() + offset[1];
            Position newPos = new Position(newRow, newCol);
            
            if (newPos.isValid() && !board.isVisited(newRow, newCol)) {
                validMoves.add(newPos);
            }
        }
        
        return validMoves;
    }

    /**
     * 获取指定位置的合法移动数量
     * 
     * @param pos 位置
     * @return 合法移动数量
     */
    public int countValidMoves(Position pos) {
        return getValidMoves(pos).size();
    }

    /**
     * 获取按Warnsdorff启发式排序的合法移动列表
     * 
     * @param from 起始位置
     * @return 按可行移动数升序排序的移动列表
     */
    public List<Position> getValidMovesWithHeuristic(Position from) {
        List<Position> validMoves = getValidMoves(from);
        
        // 按照每个位置的可行移动数量排序（升序）
        Collections.sort(validMoves, (p1, p2) -> {
            int count1 = countValidMoves(p1);
            int count2 = countValidMoves(p2);
            return Integer.compare(count1, count2);
        });
        
        return validMoves;
    }

    /**
     * 获取骑士移动偏移量数组
     * 
     * @return 移动偏移量数组
     */
    public static int[][] getMoveOffsets() {
        return MOVE_OFFSETS.clone();
    }
}
