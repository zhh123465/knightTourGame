package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import java.util.Collections;
import java.util.List;

/**
 * 深度优先搜索算法实现 (无启发式优化)
 * 
 * 为了演示效果，对候选移动进行了随机打乱，以避免陷入固定模式的局部搜索。
 */
public class DfsAlgorithm implements KnightTourAlgorithm {

    private MoveGenerator moveGenerator;

    @Override
    public List<Position> findNextMoves(Board board, Position currentPos) {
        if (moveGenerator == null || moveGenerator.getBoard() != board) {
            moveGenerator = new MoveGenerator(board);
        }
        // 获取所有合法移动
        List<Position> moves = moveGenerator.getValidMoves(currentPos);
        // 随机打乱顺序，增加找到解的随机性，并使动画更具观赏性
        Collections.shuffle(moves);
        return moves;
    }

    @Override
    public String getName() {
        return "深度优先搜索 (随机化DFS)";
    }
}
