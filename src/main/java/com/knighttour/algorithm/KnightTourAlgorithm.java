package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import java.util.List;

/**
 * 骑士巡游算法策略接口
 */
public interface KnightTourAlgorithm {
    
    /**
     * 获取当前位置的下一步可能移动列表
     * 
     * @param board 当前棋盘状态
     * @param currentPos 当前位置
     * @return 排序后的合法移动列表（排序规则由具体算法决定）
     */
    List<Position> findNextMoves(Board board, Position currentPos);
    
    /**
     * 获取算法名称（用于 UI 显示）
     * @return 算法名称
     */
    String getName();
}
