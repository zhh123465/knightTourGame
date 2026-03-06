package com.knighttour.algorithm;

import com.knighttour.model.Position;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 移动栈条目
 * 
 * 表示栈中的一个条目，包含位置、序号和未尝试的移动队列。
 * 用于非递归回溯算法中管理每个位置的状态。
 */
public class MoveStackEntry {
    
    /** 当前位置 */
    private final Position position;
    
    /** 访问序号 */
    private final int sequence;
    
    /** 未尝试的移动队列 */
    private final Queue<Position> untriedMoves;
    
    /**
     * 构造一个移动栈条目
     * 
     * @param position 当前位置
     * @param sequence 访问序号
     * @param possibleMoves 可能的移动列表
     */
    public MoveStackEntry(Position position, int sequence, List<Position> possibleMoves) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (possibleMoves == null) {
            throw new IllegalArgumentException("Possible moves cannot be null");
        }
        
        this.position = position;
        this.sequence = sequence;
        // 将列表转换为队列，便于按顺序尝试移动
        this.untriedMoves = new LinkedList<>(possibleMoves);
    }
    
    /**
     * 获取当前位置
     * 
     * @return 当前位置
     */
    public Position getPosition() {
        return position;
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
     * 检查是否还有未尝试的移动
     * 
     * @return 如果还有未尝试的移动返回 true，否则返回 false
     */
    public boolean hasUntriedMoves() {
        return !untriedMoves.isEmpty();
    }
    
    /**
     * 获取并移除下一个未尝试的移动
     * 
     * @return 下一个未尝试的移动，如果没有则返回 null
     */
    public Position getNextUntriedMove() {
        return untriedMoves.poll();
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
     * 返回栈条目的字符串表示
     * 
     * @return 包含位置、序号和未尝试移动数量的字符串
     */
    @Override
    public String toString() {
        return "Entry[pos=" + position + 
               ", seq=" + sequence + 
               ", untried=" + untriedMoves.size() + "]";
    }
}
