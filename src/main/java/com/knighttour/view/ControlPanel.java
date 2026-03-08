package com.knighttour.view;

import com.knighttour.algorithm.DfsAlgorithm;
import com.knighttour.algorithm.KnightTourAlgorithm;
import com.knighttour.algorithm.SolverState;
import com.knighttour.algorithm.WarnsdorffAlgorithm;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

import com.knighttour.util.Theme;
import com.knighttour.util.ThemeManager;

/**
 * 控制面板组件
 * 
 * 提供用户交互界面，包括起始位置输入、控制按钮和速度调节。
 */
public class ControlPanel extends VBox {

    private TextField positionInput;
    private ComboBox<KnightTourAlgorithm> algorithmComboBox;
    private ComboBox<Theme> themeComboBox;
    private CheckBox manualModeCheckBox;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button stepButton;
    private Button hintButton;
    private Button autoSolveButton;
    private Button defaultThemeButton;
    private Slider speedSlider;
    
    private Consumer<Boolean> onManualModeChangedHandler;
    private Runnable onHintHandler;
    private Runnable onAutoSolveHandler;
    private Label speedLabel;

    private Runnable onStartHandler;
    private Runnable onPauseHandler;
    private Runnable onResetHandler;
    private Runnable onStepHandler;
    private Consumer<Integer> onSpeedChangedHandler;

    public ControlPanel() {
        initializeControls();
    }

    private void initializeControls() {
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1;");

        // 1. Position Input
        VBox inputBox = new VBox(5);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        Label inputLabel = new Label("起始位置 (行,列):");
        positionInput = new TextField("0,0");
        positionInput.setPromptText("例如: 0,0");
        positionInput.setTooltip(new Tooltip("输入起始坐标，格式：行,列 (例如 0,0)"));
        inputBox.getChildren().addAll(inputLabel, positionInput);

        // 1.5 Algorithm Selection
        VBox algoBox = new VBox(5);
        algoBox.setAlignment(Pos.CENTER_LEFT);
        Label algoLabel = new Label("求解算法:");
        algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll(
            new WarnsdorffAlgorithm(),
            new DfsAlgorithm()
        );
        algorithmComboBox.getSelectionModel().selectFirst();
        // Custom cell factory to display name
        algorithmComboBox.setCellFactory(param -> new ListCell<KnightTourAlgorithm>() {
            @Override
            protected void updateItem(KnightTourAlgorithm item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        algorithmComboBox.setButtonCell(new ListCell<KnightTourAlgorithm>() {
            @Override
            protected void updateItem(KnightTourAlgorithm item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        algoBox.getChildren().addAll(algoLabel, algorithmComboBox);

        // 1.8 Theme Selection
        VBox themeBox = new VBox(5);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        Label themeLabel = new Label("背景主题:");
        HBox themeControls = new HBox(5);
        
        themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll(Theme.values());
        themeComboBox.setValue(ThemeManager.getInstance().getCurrentTheme());
        
        // Custom cell factory for Theme
        themeComboBox.setCellFactory(param -> new ListCell<Theme>() {
            @Override
            protected void updateItem(Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
        themeComboBox.setButtonCell(new ListCell<Theme>() {
            @Override
            protected void updateItem(Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
        
        // Bind theme change
        themeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ThemeManager.getInstance().setTheme(newVal);
            }
        });
        
        defaultThemeButton = new Button("恢复默认");
        defaultThemeButton.setTooltip(new Tooltip("恢复默认主题并清除配置"));
        defaultThemeButton.setOnAction(e -> {
            themeComboBox.setValue(Theme.CLASSIC);
        });
        
        themeControls.getChildren().addAll(themeComboBox, defaultThemeButton);
        themeBox.getChildren().addAll(themeLabel, themeControls);

        // 1.9 Manual Mode
        manualModeCheckBox = new CheckBox("手动模式");
        manualModeCheckBox.setTooltip(new Tooltip("开启后可手动点击棋盘进行移动"));
        manualModeCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (onManualModeChangedHandler != null) {
                onManualModeChangedHandler.accept(newVal);
            }
            updateManualControls(newVal);
        });

        // 2. Control Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        startButton = new Button("开始");
        startButton.setTooltip(new Tooltip("开始自动求解"));
        
        pauseButton = new Button("暂停");
        pauseButton.setTooltip(new Tooltip("暂停/继续求解过程"));
        
        resetButton = new Button("重置");
        resetButton.setTooltip(new Tooltip("重置棋盘和统计数据"));
        
        stepButton = new Button("单步");
        stepButton.setTooltip(new Tooltip("执行单步移动"));
        
        hintButton = new Button("提示");
        hintButton.setTooltip(new Tooltip("提示下一步最优移动"));
        hintButton.setVisible(false); // Only for manual mode
        hintButton.setManaged(false);
        
        autoSolveButton = new Button("自动求解");
        autoSolveButton.setTooltip(new Tooltip("从当前位置自动完成求解"));
        autoSolveButton.setVisible(false);
        autoSolveButton.setManaged(false);
        
        startButton.setPrefWidth(60);
        pauseButton.setPrefWidth(60);
        resetButton.setPrefWidth(60);
        stepButton.setPrefWidth(60);
        hintButton.setPrefWidth(60);
        autoSolveButton.setPrefWidth(80);
        
        // Initial state
        pauseButton.setDisable(true);
        
        buttonBox.getChildren().addAll(startButton, pauseButton);
        HBox buttonBox2 = new HBox(10);
        buttonBox2.setAlignment(Pos.CENTER);
        buttonBox2.getChildren().addAll(stepButton, resetButton);
        
        HBox manualBox = new HBox(10);
        manualBox.setAlignment(Pos.CENTER);
        manualBox.getChildren().addAll(hintButton, autoSolveButton);

        // 3. Speed Control
        VBox speedBox = new VBox(5);
        speedBox.setAlignment(Pos.CENTER_LEFT);
        Label speedTitle = new Label("动画速度 (ms):");
        speedSlider = new Slider(1, 1000, 100);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(200);
        speedSlider.setBlockIncrement(50);
        
        speedLabel = new Label("100 ms");
        
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int delay = newVal.intValue();
            speedLabel.setText(delay + " ms");
            if (onSpeedChangedHandler != null) {
                onSpeedChangedHandler.accept(delay);
            }
        });
        
        speedBox.getChildren().addAll(speedTitle, speedSlider, speedLabel);

        // Add all to main VBox
        this.getChildren().addAll(inputBox, algoBox, themeBox, manualModeCheckBox, new Separator(), buttonBox, buttonBox2, manualBox, new Separator(), speedBox);

        // Bind events
        startButton.setOnAction(e -> {
            if (onStartHandler != null) onStartHandler.run();
        });
        
        pauseButton.setOnAction(e -> {
            if (onPauseHandler != null) onPauseHandler.run();
        });
        
        resetButton.setOnAction(e -> {
            if (onResetHandler != null) onResetHandler.run();
        });
        
        stepButton.setOnAction(e -> {
            if (onStepHandler != null) onStepHandler.run();
        });
        
        hintButton.setOnAction(e -> {
            if (onHintHandler != null) onHintHandler.run();
        });
        
        autoSolveButton.setOnAction(e -> {
            if (onAutoSolveHandler != null) onAutoSolveHandler.run();
        });
    }
    
    private void updateManualControls(boolean isManual) {
        startButton.setDisable(isManual);
        pauseButton.setDisable(true); // Always disable pause when switching modes initially
        stepButton.setDisable(isManual);
        
        hintButton.setVisible(isManual);
        hintButton.setManaged(isManual);
        autoSolveButton.setVisible(isManual);
        autoSolveButton.setManaged(isManual);
        
        positionInput.setDisable(isManual);
        algorithmComboBox.setDisable(isManual);
    }

    public void setOnManualModeChanged(Consumer<Boolean> handler) {
        this.onManualModeChangedHandler = handler;
    }
    
    public void setOnHint(Runnable handler) {
        this.onHintHandler = handler;
    }
    
    public void setOnAutoSolve(Runnable handler) {
        this.onAutoSolveHandler = handler;
    }

    public void setOnStart(Runnable handler) {
        this.onStartHandler = handler;
    }

    public void setOnPause(Runnable handler) {
        this.onPauseHandler = handler;
    }

    public void setOnReset(Runnable handler) {
        this.onResetHandler = handler;
    }

    public void setOnStep(Runnable handler) {
        this.onStepHandler = handler;
    }

    public void setOnSpeedChanged(Consumer<Integer> handler) {
        this.onSpeedChangedHandler = handler;
    }

    public void setSelectedTheme(Theme theme) {
        themeComboBox.setValue(theme);
    }

    public String getPositionInput() {
        return positionInput.getText();
    }
    
    public void setPositionInput(String text) {
        positionInput.setText(text);
    }

    public KnightTourAlgorithm getSelectedAlgorithm() {
        return algorithmComboBox.getSelectionModel().getSelectedItem();
    }

    public void enableControls(boolean enable) {
        startButton.setDisable(!enable);
        stepButton.setDisable(!enable);
        positionInput.setDisable(!enable);
        algorithmComboBox.setDisable(!enable);
        // Reset and Pause are handled separately based on state
    }

    public void setButtonsState(SolverState state) {
        switch (state) {
            case IDLE:
                boolean isManual = manualModeCheckBox.isSelected();
                startButton.setText("开始");
                startButton.setDisable(isManual);
                pauseButton.setDisable(true);
                stepButton.setDisable(isManual);
                resetButton.setDisable(false);
                positionInput.setDisable(isManual);
                algorithmComboBox.setDisable(isManual);
                manualModeCheckBox.setDisable(false);
                break;
            case SOLVING:
                startButton.setDisable(true);
                pauseButton.setText("暂停");
                pauseButton.setDisable(false);
                stepButton.setDisable(true);
                resetButton.setDisable(true);
                positionInput.setDisable(true);
                algorithmComboBox.setDisable(true);
                manualModeCheckBox.setDisable(true);
                break;
            case PAUSED:
                startButton.setText("继续"); // Optional: reuse start for resume or use resume button
                startButton.setDisable(false); // Can be used to resume
                pauseButton.setText("继续");
                pauseButton.setDisable(false); // Click to resume
                stepButton.setDisable(false);
                resetButton.setDisable(false);
                algorithmComboBox.setDisable(true);
                manualModeCheckBox.setDisable(true);
                break;
            case SOLUTION_FOUND:
            case NO_SOLUTION:
                startButton.setText("开始");
                startButton.setDisable(true); // Must reset first
                pauseButton.setDisable(true);
                stepButton.setDisable(true);
                resetButton.setDisable(false);
                algorithmComboBox.setDisable(true);
                manualModeCheckBox.setDisable(false);
                break;
            default:
                // Handle STOPPED or others
                startButton.setText("开始");
                startButton.setDisable(true); // Must reset first
                pauseButton.setDisable(true);
                stepButton.setDisable(true);
                resetButton.setDisable(false);
                algorithmComboBox.setDisable(true);
                break;
        }
    }
}
