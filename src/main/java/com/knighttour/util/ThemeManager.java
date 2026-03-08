package com.knighttour.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 主题管理器
 * 
 * 负责管理当前应用主题，并分发主题变更事件。
 */
public class ThemeManager {
    
    private static final ThemeManager INSTANCE = new ThemeManager();
    
    private Theme currentTheme = Theme.CLASSIC;
    private final List<Consumer<Theme>> listeners = new ArrayList<>();
    
    private ThemeManager() {
    }
    
    public static ThemeManager getInstance() {
        return INSTANCE;
    }
    
    public Theme getCurrentTheme() {
        return currentTheme;
    }
    
    public void setTheme(Theme theme) {
        if (theme != null && theme != currentTheme) {
            this.currentTheme = theme;
            notifyListeners();
        }
    }
    
    public void addThemeChangeListener(Consumer<Theme> listener) {
        if (listener != null) {
            listeners.add(listener);
            // Optionally notify immediately
            // listener.accept(currentTheme);
        }
    }
    
    public void removeThemeChangeListener(Consumer<Theme> listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (Consumer<Theme> listener : listeners) {
            listener.accept(currentTheme);
        }
    }
}
