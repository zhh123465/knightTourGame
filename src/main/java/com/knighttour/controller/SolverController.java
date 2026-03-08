package com.knighttour.controller;

import com.knighttour.algorithm.KnightTourAlgorithm;
import com.knighttour.algorithm.KnightTourSolver;
import com.knighttour.algorithm.SolverListener;
import com.knighttour.algorithm.SolverState;
import com.knighttour.model.Board;
import com.knighttour.model.Move;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import com.knighttour.util.ThemeManager;
import com.knighttour.view.BoardView;
import com.knighttour.view.KnightAnimator;
import com.knighttour.view.StatisticsPanel;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 求解控制器
 * 
 * 负责管理求解器的执行、线程调度以及模型与视图之间的同步。
 */
public class SolverController implements SolverListener {
    
    private static final Logger logger = LoggerFactory.getLogger(SolverController.class);
    
    private final KnightTourSolver solver;
    private final BoardView boardView;
    private final StatisticsPanel statisticsPanel;
    private final KnightAnimator animator;
    private final ExecutorService solverExecutor;
    
    // Timer for real-time statistics
    private javafx.animation.AnimationTimer timer;
    private long startTime;
    private long accumulatedTime = 0;
    
    // 监听器
    private java.util.function.Consumer<Solution> onSolutionFoundHandler;
    private Runnable onNoSolutionFoundHandler;
    private java.util.function.Consumer<SolverState> onStateChangedHandler;
    
    public SolverController(Board board, BoardView boardView, StatisticsPanel statisticsPanel, KnightAnimator animator) {
        this.solver = new KnightTourSolver(board);
        this.solver.setListener(this);
        this.boardView = boardView;
        this.statisticsPanel = statisticsPanel;
        this.animator = animator;
        
        // 使用守护线程，确保应用退出时线程自动结束
        this.solverExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Solver-Thread");
                t.setDaemon(true);
                return t;
            }
        });
        
        // Initialize timer
        this.timer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                long current = System.currentTimeMillis();
                long elapsed = accumulatedTime + (current - startTime);
                statisticsPanel.updateTime(elapsed);
            }
        };
    }
    
    /**
     * 开始求解
     * 
     * @param startPos 起始位置
     * @param algorithm 使用的算法
     */
    public void solve(Position startPos, KnightTourAlgorithm algorithm) {
        if (solver.getState() == SolverState.SOLVING || solver.getState() == SolverState.PAUSED) {
            logger.warn("Solver is already running");
            return;
        }
        
        // 设置算法
        solver.setAlgorithm(algorithm);
        
        // 重置统计面板
        statisticsPanel.reset();
        boardView.resetBoard();
        
        // 放置其实位置的骑士（虽然 solve 方法也会标记，但视觉上先放置更好）
        boardView.placeKnight(startPos);
        boardView.updateCellSequence(startPos, 1);
        boardView.setCellColor(startPos, ThemeManager.getInstance().getCurrentTheme().getHighlightColor());
        
        // Start timer
        accumulatedTime = 0;
        startTime = System.currentTimeMillis();
        timer.start();
        
        // 异步执行求解
        solverExecutor.submit(() -> {
            try {
                solver.solve(startPos);
            } catch (Exception e) {
                logger.error("Error executing solver", e);
                Platform.runLater(() -> {
                   // 可以在这里显示错误对话框
                });
            }
        });
    }
    
    /**
     * 暂停求解
     */
    public void pause() {
        if (solver.getState() == SolverState.SOLVING) {
            timer.stop();
            accumulatedTime += System.currentTimeMillis() - startTime;
            solver.pause();
        }
    }
    
    /**
     * 恢复求解
     */
    public void resume() {
        if (solver.getState() == SolverState.PAUSED) {
            startTime = System.currentTimeMillis();
            timer.start();
            solver.resume();
        }
    }
    
    /**
     * 停止求解
     */
    public void stop() {
        timer.stop();
        solver.stop();
    }
    
    /**
     * 单步执行
     */
    public void step() {
        solver.step();
    }
    
    /**
     * 设置动画延迟
     * 
     * @param delayMs 延迟毫秒数
     */
    public void setAnimationDelay(int delayMs) {
        solver.setAnimationDelay(delayMs);
        animator.setAnimationDelay(delayMs);
    }
    
    public SolverState getState() {
        return solver.getState();
    }
    
    public void setOnSolutionFound(java.util.function.Consumer<Solution> handler) {
        this.onSolutionFoundHandler = handler;
    }
    
    public void setOnNoSolutionFound(Runnable handler) {
        this.onNoSolutionFoundHandler = handler;
    }
    
    public void setOnStateChanged(java.util.function.Consumer<SolverState> handler) {
        this.onStateChangedHandler = handler;
    }
    
    // --- SolverListener 实现 ---
    
    @Override
    public void onStateChanged(SolverState newState) {
        Platform.runLater(() -> {
            if (onStateChangedHandler != null) {
                onStateChangedHandler.accept(newState);
            }
        });
    }

    @Override
    public void onMoveExecuted(Move move) {
        // 在 UI 线程更新
        Platform.runLater(() -> {
            // 使用动画器移动骑士
            animator.animateMove(move.getFrom(), move.getTo(), () -> {
                // 动画完成后更新序号和颜色
                // 注意：由于 Solver 跑得很快，这里的回调可能在后续移动之后才执行
                // 但 BoardView 的状态更新应该是及时的
                // 为了避免视觉混乱，我们可以让 BoardView 的状态更新直接在下面执行
            });
            
            // 更新目标位置的状态
            boardView.updateCellSequence(move.getTo(), move.getSequence());
            // 将前一个位置设为已访问颜色
            boardView.setCellColor(move.getFrom(), ThemeManager.getInstance().getCurrentTheme().getVisitedColor());
            // 将当前位置设为当前颜色
            boardView.setCellColor(move.getTo(), ThemeManager.getInstance().getCurrentTheme().getHighlightColor());
        });
    }

    @Override
    public void onBacktrack(Move move) {
        Platform.runLater(() -> {
            // 回溯：从 move.getTo() 回到 move.getFrom()
            // move.getTo() 是那个死胡同位置
            // move.getFrom() 是上一步的位置（要回退到的位置）
            
            // 使用动画器展示回溯
            animator.animateBacktrack(move.getTo(), () -> {
                boardView.placeKnight(move.getFrom());
            });
            
            // 清除死胡同位置的标记
            boardView.clearCellSequence(move.getTo());
            // 恢复当前位置为当前颜色
            boardView.setCellColor(move.getFrom(), ThemeManager.getInstance().getCurrentTheme().getHighlightColor());
        });
    }

    @Override
    public void onSolutionFound(Solution solution) {
        Platform.runLater(() -> {
            timer.stop();
            // Update final time from solution or just stop timer
            statisticsPanel.updateTime(solution.getSolvingTimeMs());
            
            logger.info("Solution found!");
            if (onSolutionFoundHandler != null) {
                onSolutionFoundHandler.accept(solution);
            }
        });
    }

    @Override
    public void onNoSolutionFound() {
        Platform.runLater(() -> {
            timer.stop();
            logger.info("No solution found.");
            if (onNoSolutionFoundHandler != null) {
                onNoSolutionFoundHandler.run();
            }
        });
    }

    @Override
    public void onProgress(int visitedCount, int totalBacktracks) {
        Platform.runLater(() -> {
            statisticsPanel.updateVisited(visitedCount, 64);
            statisticsPanel.updateBacktracks(totalBacktracks);
            // 这里的 moves 可能不准确，需要 Solver 提供
            // SolverListener 接口中 onProgress 只提供了 visitedCount 和 totalBacktracks
            // 我们可以估算 moves = visitedCount + totalBacktracks * 2 ? 不一定
            // 或者修改 SolverListener 接口传入 totalMoves
            // 这里暂时用 visitedCount 作为 moves 的下限显示
            statisticsPanel.updateMoves(visitedCount + totalBacktracks); // 这是一个近似值，或者修改接口
        });
    }
    
    /**
     * 关闭控制器，清理资源
     */
    public void shutdown() {
        solver.stop();
        solverExecutor.shutdownNow();
    }
}
