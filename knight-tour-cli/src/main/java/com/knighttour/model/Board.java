package com.knighttour.model;

import java.util.Arrays;

/**
 * 棋盘模型，维护8x8网格状态
 * 
 * @author KnightTour
 * @version 1.0
 */
public class Board {
    private static final int SIZE = 8;
    private Cell[][] cells;
    private int visitedCount;

    /**
     * 构造函数，初始化棋盘
     */
    public Board() {
        initialize();
    }

    /**
     * 初始化棋盘
     */
    public void initialize() {
        cells = new Cell[SIZE][SIZE];
        visitedCount = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /**
     * 验证位置是否有效
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return 如果位置有效返回true，否则返回false
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * 检查位置是否已访问
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return 如果已访问返回true，否则返回false
     */
    public boolean isVisited(int row, int col) {
        if (!isValidPosition(row, col)) {
            return false;
        }
        return cells[row][col].isVisited();
    }

    /**
     * 获取指定位置的方格
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return Cell对象
     */
    public Cell getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ")");
        }
        return cells[row][col];
    }

    /**
     * 获取已访问方格数量
     * 
     * @return 已访问方格数量
     */
    public int getVisitedCount() {
        return visitedCount;
    }

    /**
     * 标记位置为已访问
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @param sequence 访问序号
     */
    public void markVisited(int row, int col, int sequence) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ")");
        }
        if (isVisited(row, col)) {
            throw new IllegalStateException("Position (" + row + ", " + col + ") is already visited");
        }
        cells[row][col].setVisited(true);
        cells[row][col].setSequence(sequence);
        visitedCount++;
    }

    /**
     * 标记位置为未访问
     * 
     * @param row 行坐标
     * @param col 列坐标
     */
    public void markUnvisited(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ")");
        }
        if (!isVisited(row, col)) {
            throw new IllegalStateException("Position (" + row + ", " + col + ") is not visited");
        }
        cells[row][col].setVisited(false);
        cells[row][col].setSequence(0);
        visitedCount--;
    }

    /**
     * 重置棋盘
     */
    public void reset() {
        initialize();
    }

    /**
     * 获取解矩阵
     * 
     * @return 8x8的解矩阵数组
     */
    public int[][] getSolutionMatrix() {
        int[][] matrix = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                matrix[row][col] = cells[row][col].getSequence();
            }
        }
        return matrix;
    }

    /**
     * 获取棋盘大小
     * 
     * @return 棋盘大小 (8)
     */
    public static int getSize() {
        return SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (isVisited(row, col)) {
                    sb.append(String.format("%3d ", cells[row][col].getSequence()));
                } else {
                    sb.append("  . ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
