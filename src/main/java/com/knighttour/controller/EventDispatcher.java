package com.knighttour.controller;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 事件分发器
 * 
 * 负责管理事件监听器并分发事件。
 */
public class EventDispatcher {
    
    private final Map<GameEvent, List<GameEventListener>> listeners;
    
    public EventDispatcher() {
        listeners = new EnumMap<>(GameEvent.class);
        // Initialize lists for all events
        for (GameEvent event : GameEvent.values()) {
            listeners.put(event, new ArrayList<>());
        }
    }
    
    /**
     * 注册事件监听器
     * 
     * @param event 要监听的事件类型
     * @param listener 监听器对象
     */
    public void addEventListener(GameEvent event, GameEventListener listener) {
        if (event != null && listener != null) {
            List<GameEventListener> eventListeners = listeners.get(event);
            if (!eventListeners.contains(listener)) {
                eventListeners.add(listener);
            }
        }
    }
    
    /**
     * 移除事件监听器
     * 
     * @param event 事件类型
     * @param listener 要移除的监听器
     */
    public void removeEventListener(GameEvent event, GameEventListener listener) {
        if (event != null && listener != null) {
            List<GameEventListener> eventListeners = listeners.get(event);
            eventListeners.remove(listener);
        }
    }
    
    /**
     * 分发事件给所有注册的监听器
     * 
     * @param event 发生的事件
     * @param data 事件携带的数据
     */
    public void dispatchEvent(GameEvent event, Object data) {
        if (event != null) {
            List<GameEventListener> eventListeners = listeners.get(event);
            // Create a copy to avoid ConcurrentModificationException if listeners are added/removed during dispatch
            List<GameEventListener> snapshot = new ArrayList<>(eventListeners);
            for (GameEventListener listener : snapshot) {
                try {
                    listener.onEvent(event, data);
                } catch (Exception e) {
                    System.err.println("Error dispatching event " + event + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 分发不带数据的事件
     * 
     * @param event 发生的事件
     */
    public void dispatchEvent(GameEvent event) {
        dispatchEvent(event, null);
    }
}
