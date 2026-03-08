package com.knighttour.model;

import java.util.Objects;

/**
 * 表示棋盘上的一个方格
 * 
 * 每个方格有固定的行列坐标，以及访问状态和访问序号。
 */
public class Cell {
    
    private final int row;
    private final int col;
    private boolean visited;
    private int sequence;  // 访问序号（1-64）
    
    /**
     * 构造一个方格对象
     * 
     * @param row 行坐标（0-7）
     * @param col 列坐标（0-7）
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.sequence = 0;
    }
    
    /**
     * 复制构造函数
     * 
     * @param other 要复制的方格
     */
    public Cell(Cell other) {
        this.row = other.row;
        this.col = other.col;
        this.visited = other.visited;
        this.sequence = other.sequence;
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
     * 判断方格是否已被访问
     * 
     * @return 如果已访问返回 true，否则返回 false
     */
    public boolean isVisited() {
        return visited;
    }
    
    /**
     * 设置方格的访问状态
     * 
     * @param visited 访问状态
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    /**
     * 获取访问序号
     * 
     * @return 访问序号（1-64），如果未访问则为 0
     */
    public int getSequence() {
        return sequence;
    }
    
    /**
     * 设置访问序号
     * 
     * @param sequence 访问序号（1-64）
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    
    /**
     * 判断两个方格是否相等（基于坐标）
     * 
     * @param o 要比较的对象
     * @return 如果行列坐标都相等返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
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
     * 返回方格的字符串表示
     * 
     * @return 包含坐标、访问状态和序号的字符串
     */
    @Override
    public String toString() {
        return "Cell(" + row + "," + col + 
               ", visited=" + visited + 
               ", seq=" + sequence + ")";
    }
}
