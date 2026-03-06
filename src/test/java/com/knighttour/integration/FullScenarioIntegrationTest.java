package com.knighttour.integration;

import com.knighttour.algorithm.KnightTourSolver;
import com.knighttour.algorithm.SolverState;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 全场景集成测试
 * 
 * 模拟用户完整操作流程：求解 -> 重置 -> 再次求解 -> 导出验证
 */
public class FullScenarioIntegrationTest {

    @Test
    public void testFullWorkflow() {
        // 1. 初始化
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        solver.setAnimationDelay(0);
        
        // 2. 第一次求解 (0,0)
        Position startPos1 = new Position(0, 0);
        System.out.println("Step 1: Solving from " + startPos1);
        Solution solution1 = solver.solve(startPos1);
        
        assertNotNull(solution1);
        assertTrue(solution1.isValid());
        assertEquals(SolverState.SOLUTION_FOUND, solver.getState());
        
        // 验证棋盘状态
        assertEquals(64, board.getVisitedCount());
        
        // 3. 重置
        System.out.println("Step 2: Resetting board");
        board.reset();
        assertEquals(0, board.getVisitedCount());
        
        // 注意：solver本身没有reset方法，状态由solve方法重置
        // 但如果我们手动重置了board，下次调用solve应该是安全的
        
        // 4. 第二次求解 (4,4)
        Position startPos2 = new Position(4, 4);
        System.out.println("Step 3: Solving from " + startPos2);
        Solution solution2 = solver.solve(startPos2);
        
        assertNotNull(solution2);
        assertTrue(solution2.isValid());
        assertEquals(SolverState.SOLUTION_FOUND, solver.getState());
        assertEquals(64, board.getVisitedCount());
        
        // 5. 验证解的格式化输出 (模拟导出)
        System.out.println("Step 4: Verifying export format");
        String output = solution2.toFormattedString();
        assertNotNull(output);
        assertTrue(output.contains("1"));
        assertTrue(output.contains("64"));
        
        // 简单验证行数
        long lines = output.lines().count();
        assertTrue(lines >= 8);
        
        System.out.println("Full workflow test passed!");
    }
}
