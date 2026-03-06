package com.knighttour.model;

/**
 * 骑士模型类
 * 
 * 表示棋盘上的骑士，维护其当前位置和移动计数。
 */
public class Knight {
    
    /** 当前行坐标 */
    private int currentRow;
    
    /** 当前列坐标 */
    private int currentCol;
    
    /** 移动次数计数 */
    private int moveCount;
    
    /**
     * 构造一个新的骑士
     * 初始位置为 (0, 0)，移动次数为 0
     */
    public Knight() {
        this.currentRow = 0;
        this.currentCol = 0;
        this.moveCount = 0;
    }
    
    /**
     * 构造一个指定位置的骑士
     * 
     * @param row 初始行坐标
     * @param col 初始列坐标
     */
    public Knight(int row, int col) {
        this.currentRow = row;
        this.currentCol = col;
        this.moveCount = 0;
    }
    
    /**
     * 移动骑士到指定位置
     * 
     * @param row 目标行坐标
     * @param col 目标列坐标
     */
    public void moveTo(int row, int col) {
        this.currentRow = row;
        this.currentCol = col;
    }
    
    /**
     * 获取骑士当前位置
     * 
     * @return 当前位置对象
     */
    public Position getCurrentPosition() {
        return new Position(currentRow, currentCol);
    }
    
    /**
     * 获取当前行坐标
     * 
     * @return 行坐标
     */
    public int getCurrentRow() {
        return currentRow;
    }
    
    /**
     * 获取当前列坐标
     * 
     * @return 列坐标
     */
    public int getCurrentCol() {
        return currentCol;
    }
    
    /**
     * 获取移动次数
     * 
     * @return 移动次数
     */
    public int getMoveCount() {
        return moveCount;
    }
    
    /**
     * 增加移动次数
     */
    public void incrementMoveCount() {
        this.moveCount++;
    }
    
    /**
     * 减少移动次数（用于回溯）
     */
    public void decrementMoveCount() {
        if (this.moveCount > 0) {
            this.moveCount--;
        }
    }
    
    /**
     * 重置移动次数
     */
    public void resetMoveCount() {
        this.moveCount = 0;
    }
    
    /**
     * 返回骑士的字符串表示
     * 
     * @return 包含位置和移动次数的字符串
     */
    @Override
    public String toString() {
        return "Knight at (" + currentRow + ", " + currentCol + 
               "), moves: " + moveCount;
    }
}
