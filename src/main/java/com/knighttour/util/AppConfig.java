package com.knighttour.util;

import java.io.Serializable;

/**
 * 应用配置类
 * 
 * 存储应用程序的所有可配置项。
 */
public class AppConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int animationDelayMs = 100;
    private String themeName = "CLASSIC";
    private boolean enableSound = false;
    private String logLevel = "INFO";
    
    /**
     * 获取默认配置
     * 
     * @return 默认配置对象
     */
    public static AppConfig getDefault() {
        return new AppConfig();
    }
    
    public int getAnimationDelayMs() {
        return animationDelayMs;
    }
    
    public void setAnimationDelayMs(int animationDelayMs) {
        // 限制范围 [0, 1000]
        this.animationDelayMs = Math.max(0, Math.min(1000, animationDelayMs));
    }
    
    public String getThemeName() {
        return themeName;
    }
    
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
    
    public boolean isEnableSound() {
        return enableSound;
    }
    
    public void setEnableSound(boolean enableSound) {
        this.enableSound = enableSound;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    @Override
    public String toString() {
        return "AppConfig{" +
                "animationDelayMs=" + animationDelayMs +
                ", themeName=" + themeName +
                ", enableSound=" + enableSound +
                ", logLevel='" + logLevel + '\'' +
                '}';
    }
}
