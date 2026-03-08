package com.knighttour.util;

import javafx.scene.paint.Color;

/**
 * 界面主题定义
 */
public enum Theme {
    
    CLASSIC("经典", Color.WHITE, Color.LIGHTGRAY, Color.BLACK, Color.YELLOW),
    DARK("深色模式", Color.rgb(50, 50, 50), Color.rgb(100, 100, 100), Color.WHITE, Color.ORANGE),
    WOOD("木纹风格", Color.rgb(222, 184, 135), Color.rgb(139, 69, 19), Color.BLACK, Color.GOLD);
    
    private final String displayName;
    private final Color lightCellColor;
    private final Color darkCellColor;
    private final Color textColor;
    private final Color highlightColor;
    
    Theme(String displayName, Color lightCellColor, Color darkCellColor, Color textColor, Color highlightColor) {
        this.displayName = displayName;
        this.lightCellColor = lightCellColor;
        this.darkCellColor = darkCellColor;
        this.textColor = textColor;
        this.highlightColor = highlightColor;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Color getLightCellColor() {
        return lightCellColor;
    }
    
    public Color getDarkCellColor() {
        return darkCellColor;
    }
    
    public Color getTextColor() {
        return textColor;
    }
    
    public Color getHighlightColor() {
        return highlightColor;
    }
}
