package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Move;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 骑士巡游问题求解器
 * 
 * 使用非递归回溯算法求解马踏棋盘问题。
 */
public class KnightTourSolver {
    
    private static final Logger logger = LoggerFactory.getLogger(KnightTourSolver.class);
    
    private final Board board;
    private final MoveStack moveStack;
    private KnightTourAlgorithm algorithm;
    private final Stack<MoveStackEntry> executionStack;
    
    private SolverState state;
    private SolverListener listener;
    private int animationDelayMs = 0;
    private boolean stepOnce = false;
    
    private final Object lock = new Object();
    
    /**
     * 构造一个求解器
     * 
     * @param board 棋盘对象
     */
    public KnightTourSolver(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.board = board;
        this.moveStack = new MoveStack();
        // 默认为 Warnsdorff 算法
        this.algorithm = new WarnsdorffAlgorithm();
        this.executionStack = new Stack<>();
        this.state = SolverState.IDLE;
    }
    
    public void setAlgorithm(KnightTourAlgorithm algorithm) {
        if (state == SolverState.SOLVING || state == SolverState.PAUSED) {
            throw new IllegalStateException("Cannot change algorithm while solving");
        }
        this.algorithm = algorithm;
    }
    
    /**
     * 设置监听器
     * 
     * @param listener 求解器监听器
     */
    public void setListener(SolverListener listener) {
        this.listener = listener;
    }
    
    /**
     * 设置动画延迟
     * 
     * @param delayMs 延迟毫秒数
     */
    public void setAnimationDelay(int delayMs) {
        this.animationDelayMs = delayMs;
    }
    
    /**
     * 获取当前状态
     * 
     * @return 求解器状态
     */
    public SolverState getState() {
        return state;
    }
    
    /**
     * 从指定位置开始求解
     * 
     * @param startPos 起始位置
     * @return 解决方案，如果无解返回 null
     */
    public Solution solve(Position startPos) {
        logger.info("Starting solver from position {}", startPos);
        
        // 初始化
        board.reset();
        moveStack.clear();
        executionStack.clear();
        
        long startTime = System.currentTimeMillis();
        int totalMoves = 0;
        int backtrackCount = 0;
        
        // 标记起始位置
        board.markVisited(startPos.getRow(), startPos.getCol(), 1);
        
        // 获取初始移动并压栈
        // 使用配置的算法获取下一步移动
        List<Position> initialMoves = algorithm.findNextMoves(board, startPos);
        MoveStackEntry initialEntry = new MoveStackEntry(startPos, 1, initialMoves);
        executionStack.push(initialEntry);
        
        state = SolverState.SOLVING;
        
        try {
            while (!executionStack.isEmpty()) {
                // 检查暂停和停止状态
                synchronized (lock) {
                    while (state == SolverState.PAUSED && !stepOnce) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    }
                    
                    if (state == SolverState.IDLE || state == SolverState.NO_SOLUTION || state == SolverState.ERROR) {
                        logger.info("Solver stopped by user or error");
                        return null;
                    }
                }
                
                // 处理单步模式：如果是因为 stepOnce 醒来，在执行完一次循环后，需要将其重置，并保持在 PAUSED 状态
                boolean isStepExecution = false;
                synchronized (lock) {
                    if (stepOnce) {
                        isStepExecution = true;
                        stepOnce = false; // 消费掉这次单步指令
                    }
                }
                
                // 检查是否找到解
                if (executionStack.size() == 64) {
                    logger.info("Solution found!");
                    state = SolverState.SOLUTION_FOUND;
                    Solution solution = new Solution(
                        board.getSolutionMatrix(),
                        moveStack.getPath(),
                        System.currentTimeMillis() - startTime,
                        totalMoves,
                        backtrackCount
                    );
                    
                    if (listener != null) {
                        listener.onSolutionFound(solution);
                    }
                    return solution;
                }
                
                MoveStackEntry currentEntry = executionStack.peek();
                
                if (currentEntry.hasUntriedMoves()) {
                    // 前进逻辑
                    Position nextPos = currentEntry.getNextUntriedMove();
                    int nextSequence = executionStack.size() + 1;
                    totalMoves++;
                    
                    // 记录移动到 moveStack (为了路径记录)
                    // 注意：这里的 Move 代表导致这一步的动作。
                    // 但是 MoveStackEntry 代表的是状态。
                    // 我们可以构建一个 Move 对象，从 currentEntry.position 到 nextPos
                    List<Position> remainingMoves = new ArrayList<>();
                    // 这里我们无法轻易获取剩余的移动列表副本，因为 currentEntry 中的 queue 是动态变化的
                    // 但对于 Move 对象来说，它应该包含当时的快照。
                    // 为了简化，这里传入空列表或者我们不严格依赖 Move 中的 untriedMoves
                    Move move = new Move(currentEntry.getPosition(), nextPos, nextSequence);
                    moveStack.push(move);
                    
                    // 标记棋盘
                    board.markVisited(nextPos.getRow(), nextPos.getCol(), nextSequence);
                    
                    // 压入新 Entry
                    List<Position> nextMoves = algorithm.findNextMoves(board, nextPos);
                    MoveStackEntry nextEntry = new MoveStackEntry(nextPos, nextSequence, nextMoves);
                    executionStack.push(nextEntry);
                    
                    // 通知监听器
                    if (listener != null) {
                        listener.onMoveExecuted(move);
                        listener.onProgress(executionStack.size(), backtrackCount);
                    }
                    
                    // 动画延迟
                    if (animationDelayMs > 0) {
                        try {
                            Thread.sleep(animationDelayMs);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    
                } else {
                    // 回溯逻辑
                    executionStack.pop(); // 弹出当前死路
                    
                    if (!executionStack.isEmpty()) {
                        backtrackCount++;
                        totalMoves++; // 回溯也算移动操作（根据某些定义），或者不算？需求12.3提到 totalMoves
                        // 需求 12.3 没有明确说回溯算不算 totalMoves，但需求 12.5 提到 "增加回溯计数"
                        // 通常 totalMoves = forward + backward
                        
                        // 从 moveStack 弹出并取消标记
                        if (!moveStack.isEmpty()) {
                            Move lastMove = moveStack.pop();
                            board.markUnvisited(lastMove.getTo().getRow(), lastMove.getTo().getCol());
                            
                            if (listener != null) {
                                listener.onBacktrack(lastMove);
                                listener.onProgress(executionStack.size(), backtrackCount);
                            }
                        }
                        
                        // 动画延迟（回溯也可能需要展示）
                        if (animationDelayMs > 0) {
                            try {
                                Thread.sleep(animationDelayMs);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    } else {
                        // 栈空了，说明无解（回到起点了）
                        // 此时 moveStack 也应该为空（或者还剩起点相关的？不，起点是在循环外标记的）
                        // 起点被标记为 visited，但不在 moveStack 中（moveStack 存的是步骤）
                        // 当 executionStack 弹出起点 Entry 时，循环结束
                        board.markUnvisited(startPos.getRow(), startPos.getCol());
                    }
                }
                
                // 如果是单步执行，执行完后需要再次检查是否需要暂停
                if (isStepExecution) {
                    synchronized (lock) {
                        // 如果状态是 PAUSED，则下次循环会再次 wait
                        // 确保 state 不变，除非它被外部修改
                    }
                }
            }
            
            // 循环结束且未找到解
            state = SolverState.NO_SOLUTION;
            logger.info("No solution found.");
            if (listener != null) {
                listener.onNoSolutionFound();
            }
            return null;
            
        } catch (Exception e) {
            state = SolverState.ERROR;
            logger.error("Error during solving", e);
            throw new RuntimeException("Solver error", e);
        }
    }
    
    /**
     * 暂停求解
     */
    public void pause() {
        synchronized (lock) {
            if (state == SolverState.SOLVING) {
                state = SolverState.PAUSED;
                logger.info("Solver paused");
            }
        }
    }
    
    /**
     * 恢复求解
     */
    public void resume() {
        synchronized (lock) {
            if (state == SolverState.PAUSED) {
                state = SolverState.SOLVING;
                lock.notifyAll();
                logger.info("Solver resumed");
            }
        }
    }
    
    /**
     * 停止求解
     */
    public void stop() {
        synchronized (lock) {
            state = SolverState.IDLE;
            lock.notifyAll();
            logger.info("Solver stopped");
        }
    }
    
    /**
     * 单步执行
     * 
     * @return 如果可以继续执行返回 true，否则返回 false
     */
    public boolean step() {
        synchronized (lock) {
            if (state == SolverState.PAUSED) {
                stepOnce = true;
                lock.notifyAll();
                return true;
            }
        }
        return false;
    }
}
