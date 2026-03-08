package com.knighttour.util;

import javafx.scene.paint.Color;

/**
 * 界面主题定义
 */
public enum Theme {
    
    CLASSIC("经典", Color.WHITE, Color.LIGHTGRAY, Color.BLACK, Color.YELLOW, Color.ORANGE),
    DARK("深色模式", Color.rgb(50, 50, 50), Color.rgb(100, 100, 100), Color.WHITE, Color.ORANGE, Color.DARKORANGE),
    WOOD("木纹风格", Color.rgb(222, 184, 135), Color.rgb(139, 69, 19), Color.BLACK, Color.GOLD, Color.GOLDENROD),
    
    // New Themes
    MINT("薄荷绿", Color.web("#F0FFF0"), Color.web("#98FF98"), Color.web("#2E8B57"), Color.web("#3CB371"), Color.web("#90EE90")),
    HAZE_BLUE("雾霾蓝", Color.web("#F0F8FF"), Color.web("#B0C4DE"), Color.web("#4682B4"), Color.web("#87CEEB"), Color.web("#ADD8E6")),
    WARM_BEIGE("暖米白", Color.web("#F5F5DC"), Color.web("#D2B48C"), Color.web("#8B4513"), Color.web("#DEB887"), Color.web("#F5DEB3")),
    SAKURA_PINK("樱花粉", Color.web("#FFF0F5"), Color.web("#FFB7C5"), Color.web("#C71585"), Color.web("#FF69B4"), Color.web("#FFB6C1")),
    LIGHT_GRAY("浅灰", Color.web("#F5F5F5"), Color.web("#D3D3D3"), Color.web("#696969"), Color.web("#A9A9A9"), Color.web("#C0C0C0"));
    
    private final String displayName;
    private final Color lightCellColor;
    private final Color darkCellColor;
    private final Color textColor;
    private final Color highlightColor;
    private final Color visitedColor;
    
    Theme(String displayName, Color lightCellColor, Color darkCellColor, Color textColor, Color highlightColor, Color visitedColor) {
        this.displayName = displayName;
        this.lightCellColor = lightCellColor;
        this.darkCellColor = darkCellColor;
        this.textColor = textColor;
        this.highlightColor = highlightColor;
        this.visitedColor = visitedColor;
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
    
    public Color getVisitedColor() {
        return visitedColor;
    }
}
