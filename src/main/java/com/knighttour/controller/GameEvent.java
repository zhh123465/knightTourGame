package com.knighttour.controller;

/**
 * 游戏事件枚举
 * 
 * 定义应用程序中可能发生的所有事件类型。
 */
public enum GameEvent {
    // 求解控制事件
    START_SOLVING,
    PAUSE_SOLVING,
    RESUME_SOLVING,
    STOP_SOLVING,
    STEP_FORWARD,
    RESET_BOARD,
    
    // 用户交互事件
    POSITION_SELECTED,
    SPEED_CHANGED,
    EXPORT_SOLUTION,
    
    // 系统事件
    SOLVER_STATE_CHANGED,
    SOLUTION_FOUND,
    NO_SOLUTION_FOUND,
    ERROR_OCCURRED
}
