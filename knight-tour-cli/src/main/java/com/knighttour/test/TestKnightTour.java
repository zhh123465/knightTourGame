package com.knighttour.test;

import com.knighttour.algorithm.KnightTourSolver;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;

/**
 * 测试类，验证马踏棋盘求解器的功能
 * 
 * @author KnightTour
 * @version 1.0
 */
public class TestKnightTour {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   马踏棋盘求解器测试");
        System.out.println("========================================\n");

        testSinglePosition(0, 0);
        testSinglePosition(4, 4);
        testSinglePosition(7, 7);
        testSinglePosition(0, 1);
        
        System.out.println("\n========================================");
        System.out.println("测试完成！");
        System.out.println("========================================");
    }

    private static void testSinglePosition(int row, int col) {
        System.out.println("\n--- 测试位置 (" + row + ", " + col + ") ---");
        
        Position startPos = new Position(row, col);
        KnightTourSolver solver = new KnightTourSolver();
        
        long startTime = System.currentTimeMillis();
        Solution solution = solver.solve(startPos);
        long endTime = System.currentTimeMillis();
        
        if (solution != null) {
            System.out.println("✓ 找到解！");
            System.out.println("解矩阵：");
            System.out.println(solution.toFormattedString());
            System.out.println("验证结果: " + (solution.isValid() ? "✓ 有效解" : "✗ 无效解"));
            System.out.println("总移动次数: " + solution.getTotalMoves());
            System.out.println("回溯次数: " + solution.getBacktrackCount());
            System.out.println("求解时间: " + String.format("%.3f", solution.getSolvingTimeSeconds()) + " 秒");
        } else {
            System.out.println("✗ 未找到解");
            System.out.println("总移动次数: " + solver.getTotalMoves());
            System.out.println("回溯次数: " + solver.getBacktrackCount());
        }
    }
}
