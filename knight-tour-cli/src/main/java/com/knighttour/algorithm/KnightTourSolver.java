package com.knighttour.algorithm;

import com.knighttour.model.*;

import java.util.*;

/**
 * 骑士巡游求解器，实现非递归回溯算法
 * 
 * @author KnightTour
 * @version 1.0
 */
public class KnightTourSolver {
    private final Board board;
    private final MoveGenerator moveGenerator;
    private final Stack<MoveStackEntry> moveStack;
    private int totalMoves;
    private int backtrackCount;
    private long startTime;
    private boolean solutionFound;
    private Solution solution;

    /**
     * 构造函数
     */
    public KnightTourSolver() {
        this.board = new Board();
        this.moveGenerator = new MoveGenerator(board);
        this.moveStack = new Stack<>();
        this.totalMoves = 0;
        this.backtrackCount = 0;
        this.solutionFound = false;
        this.solution = null;
    }

    /**
     * 求解骑士巡游问题
     * 
     * @param startPos 起始位置
     * @return 解对象，如果无解返回null
     */
    public Solution solve(Position startPos) {
        if (!startPos.isValid()) {
            throw new IllegalArgumentException("Invalid starting position: " + startPos);
        }

        reset();

        startTime = System.currentTimeMillis();
        totalMoves = 0;
        backtrackCount = 0;

        // 标记起始位置为已访问
        board.markVisited(startPos.getRow(), startPos.getCol(), 1);

        // 获取初始有效移动
        List<Position> initialMoves = moveGenerator.getValidMovesWithHeuristic(startPos);

        // 创建初始栈条目并压栈
        MoveStackEntry initialEntry = new MoveStackEntry(startPos, 1, initialMoves);
        moveStack.push(initialEntry);

        // 主循环
        while (!moveStack.isEmpty()) {
            totalMoves++;

            // 检查是否找到解
            if (moveStack.size() == 64) {
                solutionFound = true;
                long endTime = System.currentTimeMillis();
                solution = createSolution(endTime);
                return solution;
            }

            // 获取栈顶条目
            MoveStackEntry currentEntry = moveStack.peek();

            // 尝试下一步移动
            if (currentEntry.hasUntriedMoves()) {
                nextMove(currentEntry);
            } else {
                backtrack();
            }
        }

        // 无解
        long endTime = System.currentTimeMillis();
        System.out.println("No solution found after " + totalMoves + " moves and " + backtrackCount + " backtracks");
        return null;
    }

    private void nextMove(MoveStackEntry currentEntry) {
        Position nextPos = currentEntry.getNextUntriedMove();

        // 标记新位置为已访问
        int sequence = moveStack.size() + 1;
        board.markVisited(nextPos.getRow(), nextPos.getCol(), sequence);

        // 获取新位置的有效移动
        List<Position> validMoves = moveGenerator.getValidMovesWithHeuristic(nextPos);

        // 创建新条目并压栈
        MoveStackEntry newEntry = new MoveStackEntry(nextPos, sequence, validMoves);
        moveStack.push(newEntry);
    }

    private void backtrack() {
        backtrackCount++;

        // 弹出栈顶条目
        MoveStackEntry backtrackEntry = moveStack.pop();
        Position backtrackPos = backtrackEntry.getPosition();

        // 取消标记该位置
        board.markUnvisited(backtrackPos.getRow(), backtrackPos.getCol());

        // 如果栈不为空，更新当前栈顶条目
        if (!moveStack.isEmpty()) {
            MoveStackEntry currentEntry = moveStack.peek();
            // 从当前栈顶条目的未尝试列表中移除已回溯的位置
            currentEntry.getAllUntriedMoves().remove(backtrackPos);
        }
    }

    private Solution createSolution(long endTime) {
        int[][] matrix = board.getSolutionMatrix();
        List<Move> path = new ArrayList<>();
        
        // 构建移动路径
        List<MoveStackEntry> entries = new ArrayList<>(moveStack);
        for (int i = 0; i < entries.size() - 1; i++) {
            MoveStackEntry current = entries.get(i);
            MoveStackEntry next = entries.get(i + 1);
            Move move = new Move(current.getPosition(), next.getPosition(), current.getSequence(), 
                                new ArrayList<>());
            path.add(move);
        }

        long solvingTime = endTime - startTime;
        return new Solution(matrix, path, solvingTime, totalMoves, backtrackCount);
    }

    /**
     * 重置求解器
     */
    public void reset() {
        board.reset();
        moveStack.clear();
        totalMoves = 0;
        backtrackCount = 0;
        solutionFound = false;
        solution = null;
    }

    /**
     * 获取求解状态
     * 
     * @return 如果找到解返回true，否则返回false
     */
    public boolean isSolutionFound() {
        return solutionFound;
    }

    /**
     * 获取解
     * 
     * @return 解对象
     */
    public Solution getSolution() {
        return solution;
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
     * 获取棋盘
     * 
     * @return 棋盘对象
     */
    public Board getBoard() {
        return board;
    }
}
