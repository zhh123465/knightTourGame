package com.knighttour.model;

import java.util.Objects;

/**
 * 表示棋盘上的位置坐标
 * 
 * 这是一个不可变类，用于表示棋盘上的行列坐标。
 * 坐标范围为 [0,7] × [0,7]，对应8×8的棋盘。
 */
public class Position {
    
    private final int row;
    private final int col;
    
    /**
     * 构造一个位置对象
     * 
     * @param row 行坐标（0-7）
     * @param col 列坐标（0-7）
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /**
     * 获取行坐标
     * 
     * @return 行坐标
     */
    public int getRow() {
        return row;
    }
    
    /**
     * 获取列坐标
     * 
     * @return 列坐标
     */
    public int getCol() {
        return col;
    }
    
    /**
     * 验证位置是否在有效范围内
     * 
     * @return 如果行列坐标都在 [0,7] 范围内返回 true，否则返回 false
     */
    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    /**
     * 计算到另一个位置的欧几里得距离
     * 
     * @param other 另一个位置
     * @return 欧几里得距离
     */
    public double distanceTo(Position other) {
        int dr = this.row - other.row;
        int dc = this.col - other.col;
        return Math.sqrt(dr * dr + dc * dc);
    }
    
    /**
     * 判断两个位置是否相等
     * 
     * @param o 要比较的对象
     * @return 如果行列坐标都相等返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }
    
    /**
     * 计算哈希码
     * 
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
    /**
     * 返回位置的字符串表示
     * 
     * @return 格式为 "(row, col)" 的字符串
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
