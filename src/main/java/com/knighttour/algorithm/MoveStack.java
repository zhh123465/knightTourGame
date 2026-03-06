package com.knighttour.algorithm;

import com.knighttour.model.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 移动栈
 * 
 * 用于存储骑士的移动路径，支持回溯操作。
 * 这是非递归回溯算法的核心数据结构。
 */
public class MoveStack {
    
    private static final Logger logger = LoggerFactory.getLogger(MoveStack.class);
    
    /** 内部栈结构 */
    private final Stack<Move> stack;
    
    /** 最大栈大小（64个方格） */
    private final int maxSize;
    
    /**
     * 构造一个默认大小的移动栈
     * 默认最大大小为64（8×8棋盘）
     */
    public MoveStack() {
        this(64);
    }
    
    /**
     * 构造一个指定最大大小的移动栈
     * 
     * @param maxSize 最大栈大小
     */
    public MoveStack(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive");
        }
        this.maxSize = maxSize;
        this.stack = new Stack<>();
        logger.debug("MoveStack created with max size {}", maxSize);
    }
    
    /**
     * 将移动压入栈
     * 
     * @param move 要压入的移动
     * @throws IllegalStateException 如果栈已满
     */
    public void push(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("Move cannot be null");
        }
        
        if (isFull()) {
            throw new IllegalStateException("Stack is full");
        }
        
        stack.push(move);
        logger.trace("Pushed move to stack: {}, stack size: {}", move, stack.size());
    }
    
    /**
     * 从栈中弹出移动
     * 
     * @return 弹出的移动
     * @throws IllegalStateException 如果栈为空
     */
    public Move pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        
        Move move = stack.pop();
        logger.trace("Popped move from stack: {}, stack size: {}", move, stack.size());
        return move;
    }
    
    /**
     * 查看栈顶移动但不弹出
     * 
     * @return 栈顶移动
     * @throws IllegalStateException 如果栈为空
     */
    public Move peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        
        return stack.peek();
    }
    
    /**
     * 获取栈的当前大小
     * 
     * @return 栈中元素数量
     */
    public int size() {
        return stack.size();
    }
    
    /**
     * 检查栈是否为空
     * 
     * @return 如果栈为空返回 true，否则返回 false
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    /**
     * 检查栈是否已满
     * 
     * @return 如果栈已满返回 true，否则返回 false
     */
    public boolean isFull() {
        return stack.size() >= maxSize;
    }
    
    /**
     * 清空栈
     */
    public void clear() {
        int oldSize = stack.size();
        stack.clear();
        logger.debug("Stack cleared, removed {} moves", oldSize);
    }
    
    /**
     * 获取完整的移动路径
     * 
     * @return 移动路径的副本（从第一个移动到最后一个移动）
     */
    public List<Move> getPath() {
        return new ArrayList<>(stack);
    }
    
    /**
     * 获取最大栈大小
     * 
     * @return 最大栈大小
     */
    public int getMaxSize() {
        return maxSize;
    }
    
    /**
     * 返回栈的字符串表示
     * 
     * @return 包含栈大小和最大大小的字符串
     */
    @Override
    public String toString() {
        return "MoveStack[size=" + stack.size() + "/" + maxSize + "]";
    }
}
