package com.knighttour.model;

/**
 * 表示棋盘上的一个位置坐标
 * 
 * @author KnightTour
 * @version 1.0
 */
public class Position {
    private final int row;
    private final int col;

    /**
     * 构造函数
     * 
     * @param row 行坐标 (0-7)
     * @param col 列坐标 (0-7)
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
     * 验证坐标是否在棋盘范围内
     * 
     * @return 如果坐标有效返回true，否则返回false
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
