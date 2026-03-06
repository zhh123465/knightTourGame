package com.knighttour.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动模型类
 * 
 * 表示骑士的一次移动操作，包含起始位置、目标位置、序号和未尝试的移动列表。
 */
public class Move {
    
    /** 起始位置 */
    private final Position from;
    
    /** 目标位置 */
    private final Position to;
    
    /** 移动序号（1-64） */
    private final int sequence;
    
    /** 未尝试的移动列表（用于回溯） */
    private final List<Position> untriedMoves;
    
    /**
     * 构造一个移动对象
     * 
     * @param from 起始位置
     * @param to 目标位置
     * @param sequence 移动序号
     * @param untriedMoves 未尝试的移动列表
     */
    public Move(Position from, Position to, int sequence, List<Position> untriedMoves) {
        this.from = from;
        this.to = to;
        this.sequence = sequence;
        // 创建副本以避免外部修改
        this.untriedMoves = new ArrayList<>(untriedMoves);
    }
    
    /**
     * 构造一个简单的移动对象（无未尝试移动列表）
     * 
     * @param from 起始位置
     * @param to 目标位置
     * @param sequence 移动序号
     */
    public Move(Position from, Position to, int sequence) {
        this(from, to, sequence, new ArrayList<>());
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
     * 获取移动序号
     * 
     * @return 移动序号
     */
    public int getSequence() {
        return sequence;
    }
    
    /**
     * 获取未尝试的移动列表
     * 
     * @return 未尝试的移动列表的副本
     */
    public List<Position> getUntriedMoves() {
        return new ArrayList<>(untriedMoves);
    }
    
    /**
     * 移除一个未尝试的移动
     * 
     * @param pos 要移除的位置
     * @return 如果成功移除返回 true，否则返回 false
     */
    public boolean removeUntriedMove(Position pos) {
        return untriedMoves.remove(pos);
    }
    
    /**
     * 检查是否还有未尝试的移动
     * 
     * @return 如果还有未尝试的移动返回 true，否则返回 false
     */
    public boolean hasUntriedMoves() {
        return !untriedMoves.isEmpty();
    }
    
    /**
     * 获取未尝试移动的数量
     * 
     * @return 未尝试移动的数量
     */
    public int getUntriedMovesCount() {
        return untriedMoves.size();
    }
    
    /**
     * 返回移动的字符串表示
     * 
     * @return 包含起始位置、目标位置和序号的字符串
     */
    @Override
    public String toString() {
        return "Move #" + sequence + ": " + from + " -> " + to + 
               " (untried: " + untriedMoves.size() + ")";
    }
}
