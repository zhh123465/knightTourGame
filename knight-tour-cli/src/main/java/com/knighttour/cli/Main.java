package com.knighttour.cli;

import com.knighttour.algorithm.KnightTourSolver;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 命令行主程序
 * 
 * @author KnightTour
 * @version 1.0
 */
public class Main {
    private static final int BOARD_SIZE = 8;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       马踏棋盘游戏 (Knight's Tour)     ");
        System.out.println("========================================");
        System.out.println();

        while (true) {
            printMenu();
            int choice = readChoice();

            switch (choice) {
                case 1:
                    solveFromInputPosition();
                    break;
                case 2:
                    solveFromRandomPosition();
                    break;
                case 3:
                    solveAllStartingPositions();
                    break;
                case 0:
                    System.out.println("感谢使用！再见！");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效选择，请重新输入。");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("请选择操作：");
        System.out.println("1. 从指定位置开始求解");
        System.out.println("2. 从随机位置开始求解");
        System.out.println("3. 求解所有起始位置");
        System.out.println("0. 退出");
        System.out.print("请输入选项 (0-3): ");
    }

    private static int readChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static void solveFromInputPosition() {
        System.out.println("\n--- 从指定位置开始求解 ---");
        
        int row = readCoordinate("行");
        int col = readCoordinate("列");
        
        Position startPos = new Position(row, col);
        
        if (!startPos.isValid()) {
            System.out.println("错误：位置 (" + row + ", " + col + ") 无效！");
            System.out.println("有效范围：0-7");
            return;
        }

        System.out.println("\n开始求解，起始位置: " + startPos);
        System.out.println("----------------------------------------");
        
        long startTime = System.currentTimeMillis();
        KnightTourSolver solver = new KnightTourSolver();
        Solution solution = solver.solve(startPos);
        long endTime = System.currentTimeMillis();

        if (solution != null && solution.isValid()) {
            System.out.println("✓ 找到解！");
            System.out.println("\n解矩阵：");
            System.out.println(solution.toFormattedString());
            printStatistics(solution, endTime - startTime);
        } else {
            System.out.println("✗ 未找到解");
            System.out.println("总移动次数: " + solver.getTotalMoves());
            System.out.println("回溯次数: " + solver.getBacktrackCount());
        }
    }

    private static void solveFromRandomPosition() {
        System.out.println("\n--- 从随机位置开始求解 ---");
        
        int row = (int) (Math.random() * BOARD_SIZE);
        int col = (int) (Math.random() * BOARD_SIZE);
        Position startPos = new Position(row, col);
        
        System.out.println("随机起始位置: " + startPos);
        System.out.println("----------------------------------------");
        
        long startTime = System.currentTimeMillis();
        KnightTourSolver solver = new KnightTourSolver();
        Solution solution = solver.solve(startPos);
        long endTime = System.currentTimeMillis();

        if (solution != null && solution.isValid()) {
            System.out.println("✓ 找到解！");
            System.out.println("\n解矩阵：");
            System.out.println(solution.toFormattedString());
            printStatistics(solution, endTime - startTime);
        } else {
            System.out.println("✗ 未找到解");
            System.out.println("总移动次数: " + solver.getTotalMoves());
            System.out.println("回溯次数: " + solver.getBacktrackCount());
        }
    }

    private static void solveAllStartingPositions() {
        System.out.println("\n--- 求解所有起始位置 ---");
        System.out.println("这可能需要一些时间，请耐心等待...\n");
        
        int successCount = 0;
        long total_time = 0;
        long max_time = 0;
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position startPos = new Position(row, col);
                KnightTourSolver solver = new KnightTourSolver();
                long startTime = System.currentTimeMillis();
                Solution solution = solver.solve(startPos);
                long endTime = System.currentTimeMillis();
                long time = endTime - startTime;
                
                total_time += time;
                if (time > max_time) {
                    max_time = time;
                }
                
                if (solution != null && solution.isValid()) {
                    successCount++;
                    System.out.printf("位置 (%d,%d): ✓ 耗时 %.3f秒\n", row, col, time / 1000.0);
                } else {
                    System.out.printf("位置 (%d,%d): ✗ 耗时 %.3f秒\n", row, col, time / 1000.0);
                }
            }
        }
        
        System.out.println("\n========================================");
        System.out.println("统计结果：");
        System.out.println("成功求解数量: " + successCount + "/64");
        System.out.println("失败数量: " + (64 - successCount) + "/64");
        System.out.println("平均耗时: " + String.format("%.3f", total_time / 64.0) + " 秒");
        System.out.println("最大耗时: " + String.format("%.3f", max_time / 1000.0) + " 秒");
        System.out.println("========================================");
    }

    private static int readCoordinate(String coordName) {
        while (true) {
            System.out.print("请输入" + coordName + "坐标 (0-7): ");
            try {
                int value = scanner.nextInt();
                if (value >= 0 && value < BOARD_SIZE) {
                    return value;
                } else {
                    System.out.println("错误：坐标必须在 0-7 范围内！");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("错误：请输入有效的整数！");
            }
        }
    }

    private static void printStatistics(Solution solution, long elapsedTime) {
        System.out.println("\n求解统计：");
        System.out.println("总移动次数: " + solution.getTotalMoves());
        System.out.println("回溯次数: " + solution.getBacktrackCount());
        System.out.println("求解时间: " + String.format("%.3f", solution.getSolvingTimeSeconds()) + " 秒");
        System.out.println("验证结果: " + (solution.isValid() ? "✓ 有效解" : "✗ 无效解"));
    }
}
