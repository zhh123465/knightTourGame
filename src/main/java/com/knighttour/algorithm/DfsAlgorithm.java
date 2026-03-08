package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import java.util.List;

/**
 * 深度优先搜索算法实现 (无启发式优化)
 */
public class DfsAlgorithm implements KnightTourAlgorithm {

    private MoveGenerator moveGenerator;

    @Override
    public List<Position> findNextMoves(Board board, Position currentPos) {
        if (moveGenerator == null || moveGenerator.getBoard() != board) {
            moveGenerator = new MoveGenerator(board);
        }
        // 直接返回合法移动，不进行启发式排序
        return moveGenerator.getValidMoves(currentPos);
    }

    @Override
    public String getName() {
        return "深度优先搜索 (DFS)";
    }
}
