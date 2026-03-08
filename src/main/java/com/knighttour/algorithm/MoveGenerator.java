package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动生成器
 * 
 * 负责计算骑士从给定位置可以进行的所有合法移动。
 * 支持基本移动生成和启发式优化（Warnsdorff算法）。
 */
public class MoveGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(MoveGenerator.class);
    
    /**
     * 骑士的8个可能移动方向
     * 每个方向表示为 [行偏移, 列偏移]
     * 骑士移动规则：2格+1格的"日"字形
     */
    private static final int[][] MOVE_OFFSETS = {
        {2, 1},   // 右下2行1列
        {2, -1},  // 右上2行-1列
        {-2, 1},  // 左下-2行1列
        {-2, -1}, // 左上-2行-1列
        {1, 2},   // 下右1行2列
        {1, -2},  // 下左1行-2列
        {-1, 2},  // 上右-1行2列
        {-1, -2}  // 上左-1行-2列
    };
    
    /** 棋盘引用 */
    private final Board board;
    
    /**
     * 构造一个移动生成器
     * 
     * @param board 棋盘对象
     */
    public MoveGenerator(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.board = board;
        logger.debug("MoveGenerator created");
    }
    
    public Board getBoard() {
        return board;
    }
    
    /**
     * 获取从指定位置出发的所有有效移动
     * 
     * @param from 起始位置
     * @return 有效移动位置列表
     */
    public List<Position> getValidMoves(Position from) {
        if (from == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        
        List<Position> validMoves = new ArrayList<>();
        int fromRow = from.getRow();
        int fromCol = from.getCol();
        
        // 遍历所有8个可能的移动方向
        for (int[] offset : MOVE_OFFSETS) {
            int newRow = fromRow + offset[0];
            int newCol = fromCol + offset[1];
            
            // 检查新位置是否在棋盘范围内
            if (board.isValidPosition(newRow, newCol)) {
                // 检查新位置是否未被访问
                if (!board.isVisited(newRow, newCol)) {
                    validMoves.add(new Position(newRow, newCol));
                }
            }
        }
        
        logger.trace("Generated {} valid moves from {}", validMoves.size(), from);
        return validMoves;
    }
    
    /**
     * 使用Warnsdorff启发式算法获取有效移动
     * 按照每个目标位置的可行移动数量升序排序
     * 优先选择可行移动较少的位置，以减少回溯
     * 
     * @param from 起始位置
     * @return 按启发式排序的有效移动位置列表
     */
    public List<Position> getValidMovesWithHeuristic(Position from) {
        List<Position> validMoves = getValidMoves(from);
        
        // 使用Warnsdorff启发式：按可行移动数量升序排序
        validMoves.sort((p1, p2) -> {
            int count1 = countValidMoves(p1);
            int count2 = countValidMoves(p2);
            return Integer.compare(count1, count2);
        });
        
        logger.trace("Applied Warnsdorff heuristic to {} moves from {}", 
                    validMoves.size(), from);
        return validMoves;
    }
    
    /**
     * 计算从指定位置出发的有效移动数量
     * 用于Warnsdorff启发式算法
     * 
     * @param pos 位置
     * @return 有效移动数量
     */
    public int countValidMoves(Position pos) {
        int count = 0;
        int row = pos.getRow();
        int col = pos.getCol();
        
        for (int[] offset : MOVE_OFFSETS) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            
            if (board.isValidPosition(newRow, newCol) && 
                !board.isVisited(newRow, newCol)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 获取移动方向偏移数组（用于测试）
     * 
     * @return 移动方向偏移数组
     */
    public static int[][] getMoveOffsets() {
        return MOVE_OFFSETS.clone();
    }
}
