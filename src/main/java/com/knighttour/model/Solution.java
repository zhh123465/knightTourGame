package com.knighttour.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 解模型类
 * 
 * 表示骑士巡游问题的一个完整解，包含解矩阵、路径和统计信息。
 */
public class Solution {
    
    /** 解矩阵（8×8），存储每个方格的访问序号 */
    private final int[][] matrix;
    
    /** 完整的移动路径 */
    private final List<Move> path;
    
    /** 求解耗时（毫秒） */
    private final long solvingTimeMs;
    
    /** 总移动次数（包括回溯） */
    private final int totalMoves;
    
    /** 回溯次数 */
    private final int backtrackCount;
    
    /**
     * 构造一个解对象
     * 
     * @param matrix 解矩阵
     * @param path 移动路径
     * @param solvingTimeMs 求解耗时（毫秒）
     * @param totalMoves 总移动次数
     * @param backtrackCount 回溯次数
     */
    public Solution(int[][] matrix, List<Move> path, 
                   long solvingTimeMs, int totalMoves, int backtrackCount) {
        // 深拷贝矩阵
        this.matrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            this.matrix[i] = matrix[i].clone();
        }
        
        // 创建路径的不可变副本
        this.path = new ArrayList<>(path);
        
        this.solvingTimeMs = solvingTimeMs;
        this.totalMoves = totalMoves;
        this.backtrackCount = backtrackCount;
    }
    
    /**
     * 获取解矩阵
     * 
     * @return 解矩阵的副本
     */
    public int[][] getMatrix() {
        // 返回副本以保护内部状态
        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }
    
    /**
     * 获取移动路径
     * 
     * @return 移动路径的副本
     */
    public List<Move> getPath() {
        return new ArrayList<>(path);
    }
    
    /**
     * 获取求解耗时
     * 
     * @return 求解耗时（毫秒）
     */
    public long getSolvingTimeMs() {
        return solvingTimeMs;
    }
    
    /**
     * 获取总移动次数
     * 
     * @return 总移动次数（包括回溯）
     */
    public int getTotalMoves() {
        return totalMoves;
    }
    
    /**
     * 获取回溯次数
     * 
     * @return 回溯次数
     */
    public int getBacktrackCount() {
        return backtrackCount;
    }
    
    /**
     * 验证解的有效性
     * 检查矩阵是否包含1-64的所有数字且无重复
     * 
     * @return 如果解有效返回 true，否则返回 false
     */
    public boolean isValid() {
        if (matrix == null || matrix.length != 8) {
            return false;
        }
        
        Set<Integer> sequences = new HashSet<>();
        
        for (int i = 0; i < 8; i++) {
            if (matrix[i] == null || matrix[i].length != 8) {
                return false;
            }
            
            for (int j = 0; j < 8; j++) {
                int seq = matrix[i][j];
                
                // 检查序号范围
                if (seq < 1 || seq > 64) {
                    return false;
                }
                
                // 检查是否重复
                if (sequences.contains(seq)) {
                    return false;
                }
                
                sequences.add(seq);
            }
        }
        
        // 检查是否包含所有序号
        return sequences.size() == 64;
    }
    
    /**
     * 将解矩阵格式化为字符串
     * 每个数字占3个字符宽度，列对齐
     * 
     * @return 格式化的解矩阵字符串
     */
    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(String.format("%3d ", matrix[i][j]));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * 返回解的详细字符串表示
     * 
     * @return 包含统计信息和矩阵的字符串
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Solution:\n");
        sb.append("  Solving time: ").append(solvingTimeMs).append(" ms\n");
        sb.append("  Total moves: ").append(totalMoves).append("\n");
        sb.append("  Backtracks: ").append(backtrackCount).append("\n");
        sb.append("  Valid: ").append(isValid()).append("\n");
        sb.append("\nMatrix:\n");
        sb.append(toFormattedString());
        
        return sb.toString();
    }
}
