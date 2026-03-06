package com.knighttour.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动模型，表示单次移动操作
 * 
 * @author KnightTour
 * @version 1.0
 */
public class Move {
    private final Position from;
    private final Position to;
    private final int sequence;
    private final List<Position> untriedMoves;

    /**
     * 构造函数
     * 
     * @param from 起始位置
     * @param to 目标位置
     * @param sequence 访问序号
     * @param untriedMoves 未尝试的移动列表
     */
    public Move(Position from, Position to, int sequence, List<Position> untriedMoves) {
        this.from = from;
        this.to = to;
        this.sequence = sequence;
        this.untriedMoves = new ArrayList<>(untriedMoves);
    }

    /**
     * 获取起始位置
     * 
     * @return 起始位置
     */
    public Position getFrom() {
        return from;
    }

    /**
     * 获取目标位置
     * 
     * @return 目标位置
     */
    public Position getTo() {
        return to;
    }

    /**
     * 获取访问序号
     * 
     * @return 访问序号
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * 获取未尝试的移动列表
     * 
     * @return 未尝试的移动列表
     */
    public List<Position> getUntriedMoves() {
        return new ArrayList<>(untriedMoves);
    }

    /**
     * 移除已尝试的移动
     * 
     * @param pos 要移除的位置
     */
    public void removeUntriedMove(Position pos) {
        untriedMoves.remove(pos);
    }

    /**
     * 检查是否还有未尝试的移动
     * 
     * @return 如果还有未尝试的移动返回true，否则返回false
     */
    public boolean hasUntriedMoves() {
        return !untriedMoves.isEmpty();
    }

    @Override
    public String toString() {
        return "Move{" + "from=" + from + ", to=" + to + ", sequence=" + sequence + ", untriedMoves=" + untriedMoves.size() + '}';
    }
}
