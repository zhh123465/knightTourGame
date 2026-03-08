package com.knighttour.controller;

import com.knighttour.algorithm.KnightTourAlgorithm;
import com.knighttour.algorithm.SolverState;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import com.knighttour.util.AppConfig;
import com.knighttour.util.ConfigManager;
import com.knighttour.util.Theme;
import com.knighttour.util.ThemeManager;
import com.knighttour.util.Validator;
import com.knighttour.view.MainWindow;
import com.knighttour.view.SettingsDialog;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 * 主控制器
 * 
 * 协调主窗口、求解控制器和配置管理器，处理用户交互。
 */
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final MainWindow mainWindow;
    private final SolverController solverController;
    private final Board board;
    private final ConfigManager configManager;
    private final EventDispatcher eventDispatcher;
    
    private AppConfig currentConfig;
    private Solution lastSolution;
    private boolean isManualMode = false;
    private Position currentManualPos = null;
    
    public GameController(MainWindow mainWindow, SolverController solverController, Board board, ConfigManager configManager, EventDispatcher eventDispatcher) {
        this.mainWindow = mainWindow;
        this.solverController = solverController;
        this.board = board;
        this.configManager = configManager;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * 初始化控制器
     */
    public void initialize() {
        // 加载配置
        currentConfig = configManager.loadConfig();
        applyConfig(currentConfig);

        // 绑定 View 事件
        bindViewEvents();
        
        // 绑定 SolverController 回调
        solverController.setOnStateChanged(state -> {
            updateControlsState();
        });

        solverController.setOnSolutionFound(solution -> {
            this.lastSolution = solution;
            updateControlsState();
            mainWindow.showSuccessDialog("找到解了！\n耗时: " + solution.getSolvingTimeMs() + "ms");
        });
        
        solverController.setOnNoSolutionFound(() -> {
            updateControlsState();
            mainWindow.showErrorDialog("未找到解。");
        });

        // 设置初始状态
        updateControlsState();
    }
    
    // Helper to apply config
    private void applyConfig(AppConfig config) {
        solverController.setAnimationDelay(config.getAnimationDelayMs());
        
        // Apply theme
        try {
            Theme theme = Theme.valueOf(config.getThemeName());
            ThemeManager.getInstance().setTheme(theme);
            // Also update UI selection if initialized
            if (mainWindow != null && mainWindow.getControlPanel() != null) {
                mainWindow.getControlPanel().setSelectedTheme(theme);
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid theme name in config: {}", config.getThemeName());
            ThemeManager.getInstance().setTheme(Theme.CLASSIC);
        }
    }

    private void bindViewEvents() {
        // Theme events
        ThemeManager.getInstance().addThemeChangeListener(theme -> {
            if (currentConfig != null) {
                currentConfig.setThemeName(theme.name());
                try {
                    configManager.saveConfig(currentConfig);
                } catch (Exception e) {
                    logger.error("Failed to save theme config", e);
                }
            }
        });

        // ControlPanel events
        mainWindow.getControlPanel().setOnStart(this::startSolving);
        mainWindow.getControlPanel().setOnPause(this::pauseSolving);
        mainWindow.getControlPanel().setOnReset(this::resetBoard);
        mainWindow.getControlPanel().setOnStep(this::stepForward);
        
        mainWindow.getControlPanel().setOnSpeedChanged(delay -> {
            currentConfig.setAnimationDelayMs(delay);
            solverController.setAnimationDelay(delay);
        });
        
        mainWindow.getControlPanel().setOnManualModeChanged(this::setManualMode);
        mainWindow.getControlPanel().setOnHint(this::showHint);
        mainWindow.getControlPanel().setOnAutoSolve(this::autoSolve);

        // BoardView events
        mainWindow.getBoardView().setOnCellClicked(pos -> {
            if (isManualMode) {
                handleManualMove(pos);
            } else if (solverController.getState() == SolverState.IDLE) {
                mainWindow.getControlPanel().setPositionInput(pos.getRow() + "," + pos.getCol());
                // Highlight or place knight preview
                mainWindow.getBoardView().resetBoard();
                mainWindow.getBoardView().placeKnight(pos);
            }
        });

        // Menu events
        mainWindow.setOnExport(this::exportSolution);
        mainWindow.setOnSettings(this::openSettings);
        mainWindow.setOnExit(this::exit);
        mainWindow.setOnAbout(() -> {
            mainWindow.showSuccessDialog("马踏棋盘游戏 v1.0\n\n一个演示回溯算法的教学项目。");
        });
    }
    
    private void setManualMode(boolean manual) {
        this.isManualMode = manual;
        resetBoard();
        if (manual) {
            // Initialize manual mode state if needed
            currentManualPos = null;
        }
    }
    
    private void handleManualMove(Position pos) {
        if (currentManualPos == null) {
            // First move: place knight
            currentManualPos = pos;
            board.reset();
            board.markVisited(pos.getRow(), pos.getCol(), 1);
            mainWindow.getBoardView().resetBoard();
            mainWindow.getBoardView().placeKnight(pos);
            mainWindow.getBoardView().updateCellSequence(pos, 1);
            mainWindow.getBoardView().setCellColor(pos, ThemeManager.getInstance().getCurrentTheme().getHighlightColor());
            mainWindow.getStatisticsPanel().updateVisited(1, 64);
        } else {
            // Subsequent moves
            if (isValidManualMove(currentManualPos, pos)) {
                // Check solvability in background
                checkSolvabilityAndMove(pos);
            } else {
                mainWindow.showErrorDialog("无效移动！必须走'日'字且不能重复访问。");
            }
        }
    }
    
    private boolean isValidManualMove(Position from, Position to) {
        if (board.isVisited(to.getRow(), to.getCol())) return false;
        int dRow = Math.abs(from.getRow() - to.getRow());
        int dCol = Math.abs(from.getCol() - to.getCol());
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }
    
    private void checkSolvabilityAndMove(Position nextPos) {
        // Run Warnsdorff check on a COPY of the board
        new Thread(() -> {
            Board boardCopy = new Board(board);
            // Simulate move on copy
            int nextSeq = board.getVisitedCount() + 1;
            boardCopy.markVisited(nextPos.getRow(), nextPos.getCol(), nextSeq);
            
            // We use a temporary solver to check solvability
            // Or just use WarnsdorffAlgorithm directly to see if we can reach end?
            // Actually, "solvability" means "can we complete the tour from here?"
            // That requires running the solver.
            // For 8x8, running solver might take time if we don't use Warnsdorff.
            // Using Warnsdorff is fast.
            
            com.knighttour.algorithm.KnightTourSolver tempSolver = new com.knighttour.algorithm.KnightTourSolver(boardCopy);
            tempSolver.setAlgorithm(new com.knighttour.algorithm.WarnsdorffAlgorithm());
            Solution sol = tempSolver.solve(nextPos, false); // solve remaining
            
            Platform.runLater(() -> {
                if (sol != null) {
                    // Valid move
                    int seq = board.getVisitedCount() + 1;
                    mainWindow.getBoardView().setCellColor(currentManualPos, ThemeManager.getInstance().getCurrentTheme().getVisitedColor());
                    
                    board.markVisited(nextPos.getRow(), nextPos.getCol(), seq);
                    currentManualPos = nextPos;
                    
                    mainWindow.getBoardView().placeKnight(nextPos);
                    mainWindow.getBoardView().updateCellSequence(nextPos, seq);
                    mainWindow.getBoardView().setCellColor(nextPos, ThemeManager.getInstance().getCurrentTheme().getHighlightColor());
                    mainWindow.getStatisticsPanel().updateVisited(seq, 64);
                    
                    if (seq == 64) {
                        mainWindow.showSuccessDialog("恭喜！手动完成了骑士巡游！");
                    }
                } else {
                    // Invalid path
                    mainWindow.showErrorDialog("当前路径无解！\n系统已自动阻止该移动。");
                    // TODO: Mark invalid path visually
                }
            });
        }).start();
    }
    
    private void showHint() {
        if (currentManualPos == null) return;
        
        new Thread(() -> {
            // Find best move using Warnsdorff
            com.knighttour.algorithm.WarnsdorffAlgorithm algo = new com.knighttour.algorithm.WarnsdorffAlgorithm();
            java.util.List<Position> moves = algo.findNextMoves(board, currentManualPos);
            
            if (!moves.isEmpty()) {
                Position bestMove = moves.get(0);
                Platform.runLater(() -> {
                    // Blink effect
                    mainWindow.getBoardView().highlightCell(bestMove);
                });
            }
        }).start();
    }
    
    private void autoSolve() {
        if (currentManualPos == null) return;
        
        solverController.solve(currentManualPos, new com.knighttour.algorithm.WarnsdorffAlgorithm());
    }

    private void startSolving() {
        try {
            Position startPos = parseCurrentPosition();
            KnightTourAlgorithm algorithm = mainWindow.getControlPanel().getSelectedAlgorithm();
            mainWindow.getControlPanel().enableControls(false); // Disable inputs
            solverController.solve(startPos, algorithm);
            updateControlsState();
        } catch (Exception e) {
            mainWindow.showErrorDialog(e.getMessage());
        }
    }

    private void pauseSolving() {
        if (solverController.getState() == SolverState.SOLVING) {
            solverController.pause();
        } else if (solverController.getState() == SolverState.PAUSED) {
            solverController.resume();
        }
        updateControlsState();
    }

    private void resetBoard() {
        solverController.stop();
        mainWindow.getBoardView().resetBoard();
        mainWindow.getStatisticsPanel().reset();
        updateControlsState();
    }
    
    private void stepForward() {
        solverController.step();
        updateControlsState();
    }

    private Position parseCurrentPosition() {
        String input = mainWindow.getControlPanel().getPositionInput();
        try {
            return Validator.parsePosition(input);
        } catch (Exception e) {
            // 这里我们只是在内部调用，如果解析失败返回 null 或者默认值
            // 但是 startSolving 中会处理异常，所以这里最好抛出 unchecked exception 或者在调用处处理
            // 为了简化，这里抛出 RuntimeException 包装
            throw new RuntimeException(e);
        }
    }

    private void updateControlsState() {
        SolverState state = solverController.getState();
        mainWindow.getControlPanel().setButtonsState(state);
        
        // Enable/disable menu items based on state if possible
    }
    
    public void exportSolution() {
        if (lastSolution == null) {
            mainWindow.showErrorDialog("没有可导出的解。请先运行求解。");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存解矩阵");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(mainWindow);
        
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(lastSolution.toFormattedString());
                mainWindow.showSuccessDialog("导出成功: " + file.getName());
            } catch (IOException e) {
                logger.error("Export failed", e);
                mainWindow.showErrorDialog("导出失败: " + e.getMessage());
            }
        }
    }
    
    public void openSettings() {
        SettingsDialog dialog = new SettingsDialog(currentConfig);
        Optional<AppConfig> result = dialog.showAndWait();
        
        result.ifPresent(newConfig -> {
            this.currentConfig = newConfig;
            applyConfig(newConfig);
            try {
                configManager.saveConfig(newConfig);
            } catch (Exception e) {
                logger.error("Failed to save config", e);
                mainWindow.showErrorDialog("保存配置失败: " + e.getMessage());
            }
        });
    }
    
    /**
     * 退出应用
     */
    public void exit() {
        solverController.shutdown();
        mainWindow.close();
    }
}
