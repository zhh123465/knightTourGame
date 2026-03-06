package com.knighttour.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * 统计面板组件
 * 
 * 显示求解过程中的实时统计信息，如已访问方格数、移动步数、回溯次数和耗时。
 */
public class StatisticsPanel extends HBox {

    private Label visitedLabel;
    private Label movesLabel;
    private Label backtrackLabel;
    private Label timeLabel;

    public StatisticsPanel() {
        initializeLabels();
    }

    private void initializeLabels() {
        this.setSpacing(20);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");

        visitedLabel = new Label("已访问: 0 / 64");
        movesLabel = new Label("总步数: 0");
        backtrackLabel = new Label("回溯次数: 0");
        timeLabel = new Label("耗时: 0 ms");

        // Use regions to space out labels evenly if needed, or just HBox spacing
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        this.getChildren().addAll(visitedLabel, spacer1, movesLabel, spacer2, backtrackLabel, spacer3, timeLabel);
    }

    public void updateVisited(int visited, int total) {
        visitedLabel.setText(String.format("已访问: %d / %d", visited, total));
    }

    public void updateMoves(int moves) {
        movesLabel.setText("总步数: " + moves);
    }

    public void updateBacktracks(int backtracks) {
        backtrackLabel.setText("回溯次数: " + backtracks);
    }

    public void updateTime(long milliseconds) {
        timeLabel.setText("耗时: " + milliseconds + " ms");
    }

    public void reset() {
        updateVisited(0, 64);
        updateMoves(0);
        updateBacktracks(0);
        updateTime(0);
    }
}
