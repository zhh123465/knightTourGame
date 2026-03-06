package com.knighttour.integration;

import com.knighttour.algorithm.KnightTourSolver;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试：全位置求解
 * 
 * 验证从棋盘上所有64个位置开始都能找到解。
 */
public class AllPositionsIntegrationTest {

    @Test
    public void testAllStartingPositions() {
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        
        // 禁用动画延迟以加快测试速度
        solver.setAnimationDelay(0);
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position startPos = new Position(row, col);
                System.out.println("Testing start position: " + startPos);
                
                long startTime = System.currentTimeMillis();
                Solution solution = solver.solve(startPos);
                long duration = System.currentTimeMillis() - startTime;
                
                assertNotNull(solution, "Should find solution for position " + startPos);
                assertTrue(solution.isValid(), "Solution should be valid for position " + startPos);
                
                System.out.println("Found solution for " + startPos + " in " + duration + " ms");
            }
        }
    }
}
