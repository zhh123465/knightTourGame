package com.knighttour.algorithm;

import com.knighttour.model.Move;
import com.knighttour.model.Solution;

/**
 * 求解器监听器接口
 * 
 * 用于监听求解过程中的各种事件，支持GUI更新和动画。
 */
public interface SolverListener {
    
    /**
     * 当执行一次移动时调用
     * 
     * @param move 执行的移动
     */
    void onMoveExecuted(Move move);
    
    /**
     * 当发生回溯时调用
     * 
     * @param move 回溯的移动
     */
    void onBacktrack(Move move);
    
    /**
     * 当找到解时调用
     * 
     * @param solution 找到的解
     */
    void onSolutionFound(Solution solution);
    
    /**
     * 当无法找到解时调用
     */
    void onNoSolutionFound();
    
    /**
     * 当求解进度更新时调用
     * 
     * @param visitedCount 已访问方格数量
     * @param totalBacktracks 总回溯次数
     */
    void onProgress(int visitedCount, int totalBacktracks);
    
    /**
     * 当求解器状态发生变化时调用
     * 
     * @param newState 新的状态
     */
    void onStateChanged(SolverState newState);
}
