package com.knighttour.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 解模型，存储完整的解矩阵
 * 
 * @author KnightTour
 * @version 1.0
 */
public class Solution {
    private final int[][] matrix;
    private final List<Move> path;
    private final long solvingTimeMs;
    private final int totalMoves;
    private final int backtrackCount;

    /**
     * 构造函数
     * 
     * @param matrix 解矩阵
     * @param path 移动路径
     * @param solvingTimeMs 求解时间（毫秒）
     * @param totalMoves 总移动次数
     * @param backtrackCount 回溯次数
     */
    public Solution(int[][] matrix, List<Move> path, long solvingTimeMs, int totalMoves, int backtrackCount) {
        this.matrix = deepCopyMatrix(matrix);
        this.path = new ArrayList<>(path);
        this.solvingTimeMs = solvingTimeMs;
        this.totalMoves = totalMoves;
        this.backtrackCount = backtrackCount;
    }

    private int[][] deepCopyMatrix(int[][] original) {
        if (original == null) {
            return null;
        }
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    /**
     * 获取解矩阵
     * 
     * @return 解矩阵
     */
    public int[][] getMatrix() {
        return deepCopyMatrix(matrix);
    }

    /**
     * 获取移动路径
     * 
     * @return 移动路径
     */
    public List<Move> getPath() {
        return new ArrayList<>(path);
    }

    /**
     * 获取求解时间（毫秒）
     * 
     * @return 求解时间
     */
    public long getSolvingTimeMs() {
        return solvingTimeMs;
    }

    /**
     * 获取总移动次数
     * 
     * @return 总移动次数
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
     * 
     * @return 如果解有效返回true，否则返回false
     */
    public boolean isValid() {
        java.util.Set<Integer> sequences = new java.util.HashSet<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int seq = matrix[i][j];
                if (seq < 1 || seq > 64 || sequences.contains(seq)) {
                    return false;
                }
                sequences.add(seq);
            }
        }
        return sequences.size() == 64;
    }

    /**
     * 格式化输出解矩阵
     * 
     * @return 格式化的字符串
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
     * 获取求解时间（秒）
     * 
     * @return 求解时间（秒）
     */
    public double getSolvingTimeSeconds() {
        return solvingTimeMs / 1000.0;
    }
}
