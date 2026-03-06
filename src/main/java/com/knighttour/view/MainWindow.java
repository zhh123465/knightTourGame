package com.knighttour.view;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 主窗口组件
 * 
 * 整合棋盘、控制面板和统计面板，提供应用程序的主界面。
 */
public class MainWindow extends Stage {

    private BoardView boardView;
    private ControlPanel controlPanel;
    private StatisticsPanel statisticsPanel;
    private MenuBar menuBar;

    private MenuItem exportItem;
    private MenuItem exitItem;
    private MenuItem settingsItem;
    private MenuItem aboutItem;

    public MainWindow() {
        initializeComponents();
        initializeLayout();
    }

    private void initializeComponents() {
        boardView = new BoardView();
        controlPanel = new ControlPanel();
        statisticsPanel = new StatisticsPanel();
        
        // Initialize MenuBar
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("文件");
        exportItem = new MenuItem("导出解矩阵");
        exitItem = new MenuItem("退出");
        fileMenu.getItems().addAll(exportItem, exitItem);
        
        Menu viewMenu = new Menu("视图");
        settingsItem = new MenuItem("设置");
        viewMenu.getItems().add(settingsItem);
        
        Menu helpMenu = new Menu("帮助");
        aboutItem = new MenuItem("关于");
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        
        // Bind basic events (controller will override or bind more specific logic)
        exitItem.setOnAction(e -> this.close());
    }

    public void setOnExport(Runnable handler) {
        exportItem.setOnAction(e -> {
            if (handler != null) handler.run();
        });
    }

    public void setOnSettings(Runnable handler) {
        settingsItem.setOnAction(e -> {
            if (handler != null) handler.run();
        });
    }

    public void setOnAbout(Runnable handler) {
        aboutItem.setOnAction(e -> {
            if (handler != null) handler.run();
        });
    }

    public void setOnExit(Runnable handler) {
        exitItem.setOnAction(e -> {
            if (handler != null) handler.run();
        });
    }

    private void initializeLayout() {
        BorderPane root = new BorderPane();
        
        root.setTop(menuBar);
        root.setCenter(boardView);
        root.setRight(controlPanel);
        root.setBottom(statisticsPanel);
        
        Scene scene = new Scene(root, 900, 700);
        this.setScene(scene);
        this.setTitle("马踏棋盘游戏");
        this.setResizable(false);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }
    
    public void showErrorDialog(String message) {
        // Simple alert
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showSuccessDialog(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
