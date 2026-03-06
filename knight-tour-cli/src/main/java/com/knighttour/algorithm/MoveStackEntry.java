package com.knighttour.algorithm;

import com.knighttour.model.Move;
import com.knighttour.model.Position;

import java.util.*;

/**
 * 移动栈条目，用于非递归回溯算法
 * 
 * @author KnightTour
 * @version 1.0
 */
public class MoveStackEntry {
    private final Position position;
    private final int sequence;
    private final Queue<Position> untriedMoves;

    /**
     * 构造函数
     * 
     * @param position 位置
     * @param sequence 序号
     * @param possibleMoves 可能的移动列表
     */
    public MoveStackEntry(Position position, int sequence, List<Position> possibleMoves) {
        this.position = position;
        this.sequence = sequence;
        this.untriedMoves = new LinkedList<>(possibleMoves);
    }

    /**
     * 获取位置
     * 
     * @return 位置
     */
    public Position getPosition() {
        return position;
    }

    /**
     * 获取序号
     * 
     * @return 序号
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * 检查是否还有未尝试的移动
     * 
     * @return 如果还有未尝试的移动返回true，否则返回false
     */
    public boolean hasUntriedMoves() {
        return !untriedMoves.isEmpty();
    }

    /**
     * 获取下一个未尝试的移动
     * 
     * @return 下一个未尝试的移动位置
     */
    public Position getNextUntriedMove() {
        return untriedMoves.poll();
    }

    /**
     * 获取未尝试移动数量
     * 
     * @return 未尝试移动数量
     */
    public int getUntriedMovesCount() {
        return untriedMoves.size();
    }

    /**
     * 获取所有未尝试的移动
     * 
     * @return 未尝试移动列表
     */
    public List<Position> getAllUntriedMoves() {
        return new ArrayList<>(untriedMoves);
    }
}
