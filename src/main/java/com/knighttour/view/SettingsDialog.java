package com.knighttour.view;

import com.knighttour.util.AppConfig;
import com.knighttour.util.Theme;
import com.knighttour.util.ThemeManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * 设置对话框
 * 
 * 允许用户修改应用程序配置，如动画速度和颜色方案。
 */
public class SettingsDialog extends Dialog<AppConfig> {

    private Slider speedSlider;
    private CheckBox soundCheckBox;
    private ComboBox<Theme> themeComboBox;

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

        // Theme
        themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll(Theme.values());
        try {
            themeComboBox.setValue(Theme.valueOf(currentConfig.getThemeName()));
        } catch (IllegalArgumentException e) {
            themeComboBox.setValue(Theme.CLASSIC);
        }
        
        // Custom cell factory for Theme display
        Callback<ListView<Theme>, ListCell<Theme>> cellFactory = param -> new ListCell<Theme>() {
            @Override
            protected void updateItem(Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        };
        themeComboBox.setCellFactory(cellFactory);
        themeComboBox.setButtonCell(cellFactory.call(null));
        
        grid.add(new Label("主题:"), 0, 2);
        grid.add(themeComboBox, 1, 2);

        this.getDialogPane().setContent(grid);

        // Convert the result to AppConfig when the save button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                AppConfig newConfig = new AppConfig();
                newConfig.setAnimationDelayMs((int) speedSlider.getValue());
                newConfig.setEnableSound(soundCheckBox.isSelected());
                newConfig.setThemeName(themeComboBox.getValue().name());
                return newConfig;
            }
            return null;
        });
    }
}
