package com.knighttour.model;

/**
 * 表示棋盘上的一个方格
 * 
 * @author KnightTour
 * @version 1.0
 */
public class Cell {
    private final int row;
    private final int col;
    private boolean visited;
    private int sequence;

    /**
     * 构造函数
     * 
     * @param row 行坐标
     * @param col 列坐标
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.sequence = 0;
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
     * 获取是否已访问
     * 
     * @return 如果已访问返回true，否则返回false
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * 设置访问状态
     * 
     * @param visited 访问状态
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * 获取访问序号
     * 
     * @return 访问序号 (1-64)
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * 设置访问序号
     * 
     * @param sequence 访问序号
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "Cell{" + "row=" + row + ", col=" + col + ", visited=" + visited + ", sequence=" + sequence + '}';
    }
}
