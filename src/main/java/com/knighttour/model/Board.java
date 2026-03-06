package com.knighttour.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 棋盘模型类
 * 
 * 维护8×8棋盘的状态，管理方格的访问记录。
 */
public class Board {
    
    private static final Logger logger = LoggerFactory.getLogger(Board.class);
    
    /** 棋盘大小（8×8） */
    public static final int SIZE = 8;
    
    /** 棋盘方格数组 */
    private Cell[][] cells;
    
    /** 已访问方格计数 */
    private int visitedCount;
    
    /**
     * 构造一个新的棋盘
     * 自动调用 initialize() 初始化棋盘
     */
    public Board() {
        initialize();
    }
    
    /**
     * 初始化棋盘
     * 创建8×8的方格数组，所有方格初始为未访问状态
     */
    public void initialize() {
        logger.debug("Initializing board");
        cells = new Cell[SIZE][SIZE];
        visitedCount = 0;
        
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        
        logger.debug("Board initialized with {} cells", SIZE * SIZE);
    }
    
    /**
     * 验证位置是否在棋盘范围内
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return 如果位置有效返回 true，否则返回 false
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
    
    /**
     * 检查指定位置是否已被访问
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return 如果已访问返回 true，否则返回 false
     * @throws IllegalArgumentException 如果位置无效
     */
    public boolean isVisited(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                "Invalid position: (" + row + ", " + col + ")");
        }
        return cells[row][col].isVisited();
    }
    
    /**
     * 获取指定位置的方格
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return 方格对象
     * @throws IllegalArgumentException 如果位置无效
     */
    public Cell getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                "Invalid position: (" + row + ", " + col + ")");
        }
        return cells[row][col];
    }
    
    /**
     * 获取已访问方格的数量
     * 
     * @return 已访问方格数量
     */
    public int getVisitedCount() {
        return visitedCount;
    }
    
    /**
     * 获取棋盘大小
     * 
     * @return 棋盘大小（8）
     */
    public int getSize() {
        return SIZE;
    }
    
    /**
     * 标记指定位置为已访问
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @param sequence 访问序号（1-64）
     * @throws IllegalArgumentException 如果位置无效或已被访问
     */
    public void markVisited(int row, int col, int sequence) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                "Invalid position: (" + row + ", " + col + ")");
        }
        
        Cell cell = cells[row][col];
        if (cell.isVisited()) {
            logger.warn("Cell ({}, {}) is already visited", row, col);
            throw new IllegalArgumentException(
                "Cell (" + row + ", " + col + ") is already visited");
        }
        
        cell.setVisited(true);
        cell.setSequence(sequence);
        visitedCount++;
        
        logger.trace("Marked cell ({}, {}) as visited with sequence {}", 
                    row, col, sequence);
    }
    
    /**
     * 取消标记指定位置（用于回溯）
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @throws IllegalArgumentException 如果位置无效或未被访问
     */
    public void markUnvisited(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                "Invalid position: (" + row + ", " + col + ")");
        }
        
        Cell cell = cells[row][col];
        if (!cell.isVisited()) {
            logger.warn("Cell ({}, {}) is not visited", row, col);
            throw new IllegalArgumentException(
                "Cell (" + row + ", " + col + ") is not visited");
        }
        
        cell.setVisited(false);
        cell.setSequence(0);
        visitedCount--;
        
        logger.trace("Unmarked cell ({}, {}) as unvisited", row, col);
    }
    
    /**
     * 重置棋盘到初始状态
     * 清除所有访问记录
     */
    public void reset() {
        logger.debug("Resetting board");
        
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setVisited(false);
                cells[row][col].setSequence(0);
            }
        }
        
        visitedCount = 0;
        logger.debug("Board reset complete");
    }
    
    /**
     * 获取解矩阵
     * 返回一个8×8的整数数组，包含每个方格的访问序号
     * 
     * @return 解矩阵，未访问的方格序号为0
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
     * 返回棋盘的字符串表示（用于调试）
     * 
     * @return 棋盘状态的字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board (").append(visitedCount).append("/64 visited):\n");
        
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int seq = cells[row][col].getSequence();
                if (seq > 0) {
                    sb.append(String.format("%3d ", seq));
                } else {
                    sb.append("  . ");
                }
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
