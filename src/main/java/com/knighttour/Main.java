package com.knighttour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 马踏棋盘游戏主入口类
 * 
 * 这是JavaFX应用程序的主类，负责启动应用程序并初始化主窗口。
 */
public class Main extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    /**
     * JavaFX应用程序启动方法
     * 
     * @param primaryStage 主舞台
     */
    private com.knighttour.controller.GameController gameController;

    @Override
    public void start(Stage primaryStage) {
        // 设置全局异常处理器
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            logger.error("Uncaught exception in thread " + t.getName(), e);
            Platform.runLater(() -> showErrorDialog("未捕获的异常", e.getMessage()));
        });

        try {
            logger.info("Knight Tour Game starting...");
            
            // Initialize components
            com.knighttour.model.Board board = new com.knighttour.model.Board();
            com.knighttour.util.ConfigManager configManager = new com.knighttour.util.ConfigManager();
            com.knighttour.controller.EventDispatcher eventDispatcher = new com.knighttour.controller.EventDispatcher();
            
            com.knighttour.view.MainWindow mainWindow = new com.knighttour.view.MainWindow();
            com.knighttour.view.KnightAnimator animator = new com.knighttour.view.KnightAnimator(mainWindow.getBoardView());
            
            com.knighttour.controller.SolverController solverController = new com.knighttour.controller.SolverController(
                board, 
                mainWindow.getBoardView(), 
                mainWindow.getStatisticsPanel(), 
                animator
            );
            
            gameController = new com.knighttour.controller.GameController(
                mainWindow, 
                solverController, 
                board, 
                configManager, 
                eventDispatcher
            );
            
            gameController.initialize();
            
            mainWindow.show();
            
            logger.info("Knight Tour Game started successfully");
            
        } catch (Exception e) {
            logger.error("Failed to start Knight Tour Game", e);
            throw new RuntimeException("Application startup failed", e);
        }
    }
    
    @Override
    public void stop() {
        logger.info("Knight Tour Game stopping...");
        if (gameController != null) {
            gameController.exit();
        }
        logger.info("Knight Tour Game stopped");
    }
    
    /**
     * 主方法，程序入口点
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        logger.info("Launching Knight Tour Game application");
        launch(args);
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message != null ? message : "发生未知错误");
        alert.showAndWait();
    }
}
