package com.knighttour.util;

import javafx.scene.paint.Color;

/**
 * 颜色方案
 * 
 * 定义棋盘和动画的颜色配置。
 */
public class ColorScheme {
    
    /** 默认颜色方案 */
    public static final ColorScheme DEFAULT = new ColorScheme(
        Color.WHITE,      // 未访问方格
        Color.LIGHTGRAY,  // 已访问方格
        Color.BLUE,       // 当前位置
        Color.GREEN       // 完成路径
    );
    
    private final Color unvisitedColor;
    private final Color visitedColor;
    private final Color currentColor;
    private final Color completedColor;
    
    /**
     * 构造一个颜色方案
     * 
     * @param unvisited 未访问方格颜色
     * @param visited 已访问方格颜色
     * @param current 当前位置颜色
     * @param completed 完成路径颜色
     */
    public ColorScheme(Color unvisited, Color visited, Color current, Color completed) {
        this.unvisitedColor = unvisited;
        this.visitedColor = visited;
        this.currentColor = current;
        this.completedColor = completed;
    }
    
    public Color getUnvisitedColor() { return unvisitedColor; }
    public Color getVisitedColor() { return visitedColor; }
    public Color getCurrentColor() { return currentColor; }
    public Color getCompletedColor() { return completedColor; }
    
    @Override
    public String toString() {
        return String.format("ColorScheme[unvisited=%s, visited=%s, current=%s, completed=%s]",
            unvisitedColor, visitedColor, currentColor, completedColor);
    }
}
