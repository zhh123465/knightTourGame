package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import java.util.List;

/**
 * Warnsdorff 启发式算法实现
 */
public class WarnsdorffAlgorithm implements KnightTourAlgorithm {

    private MoveGenerator moveGenerator;

    @Override
    public List<Position> findNextMoves(Board board, Position currentPos) {
        if (moveGenerator == null || moveGenerator.getBoard() != board) {
            moveGenerator = new MoveGenerator(board);
        }
        return moveGenerator.getValidMovesWithHeuristic(currentPos);
    }

    @Override
    public String getName() {
        return "Warnsdorff 启发式";
    }
}
