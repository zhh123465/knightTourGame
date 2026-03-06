package com.knighttour.view;

import com.knighttour.util.AppConfig;
import com.knighttour.util.ColorScheme;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * 设置对话框
 * 
 * 允许用户修改应用程序配置，如动画速度和颜色方案。
 */
public class SettingsDialog extends Dialog<AppConfig> {

    private Slider speedSlider;
    private CheckBox soundCheckBox;
    private ComboBox<String> colorSchemeCombo;

    public SettingsDialog(AppConfig currentConfig) {
        this.setTitle("设置");
        this.setHeaderText("修改应用程序配置");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("保存", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Speed Slider
        speedSlider = new Slider(0, 1000, currentConfig.getAnimationDelayMs());
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        
        grid.add(new Label("动画速度 (ms):"), 0, 0);
        grid.add(speedSlider, 1, 0);

        // Sound Checkbox
        soundCheckBox = new CheckBox("启用音效");
        soundCheckBox.setSelected(currentConfig.isEnableSound());
        
        grid.add(new Label("音效:"), 0, 1);
        grid.add(soundCheckBox, 1, 1);

        // Color Scheme
        colorSchemeCombo = new ComboBox<>();
        colorSchemeCombo.getItems().addAll("默认", "高对比度");
        colorSchemeCombo.setValue("默认"); // Default for now as we don't have logic to identify current scheme by name easily
        
        grid.add(new Label("颜色方案:"), 0, 2);
        grid.add(colorSchemeCombo, 1, 2);

        this.getDialogPane().setContent(grid);

        // Convert the result to AppConfig when the save button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                AppConfig newConfig = new AppConfig();
                newConfig.setAnimationDelayMs((int) speedSlider.getValue());
                newConfig.setEnableSound(soundCheckBox.isSelected());
                
                // Handle color scheme selection
                if ("高对比度".equals(colorSchemeCombo.getValue())) {
                    newConfig.setColorScheme(new ColorScheme(
                        Color.BLACK, Color.WHITE, Color.YELLOW, Color.RED
                    ));
                } else {
                    newConfig.setColorScheme(ColorScheme.DEFAULT);
                }
                
                return newConfig;
            }
            return null;
        });
    }
}
