package com.knighttour.algorithm;

/**
 * 求解器状态枚举
 * 
 * 表示求解器的当前状态。
 */
public enum SolverState {
    
    /** 空闲状态 - 未开始求解 */
    IDLE,
    
    /** 正在求解 */
    SOLVING,
    
    /** 已暂停 */
    PAUSED,
    
    /** 找到解 */
    SOLUTION_FOUND,
    
    /** 无解 */
    NO_SOLUTION,
    
    /** 错误状态 */
    ERROR
}
