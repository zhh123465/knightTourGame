package com.knighttour.controller;

/**
 * 游戏事件监听器接口
 * 
 * 用于接收和处理游戏事件。
 */
public interface GameEventListener {
    
    /**
     * 当事件发生时调用
     * 
     * @param event 事件类型
     * @param data 事件携带的数据（可选，可能为null）
     */
    void onEvent(GameEvent event, Object data);
}
