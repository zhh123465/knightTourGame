package com.knighttour.integration;

import com.knighttour.algorithm.KnightTourSolver;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 性能测试
 * 
 * 测量所有起始位置的求解时间，并统计平均值和最大值。
 */
public class PerformanceTest {

    @Test
    public void benchmarkSolving() {
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        solver.setAnimationDelay(0);
        
        List<Long> durations = new ArrayList<>();
        long maxDuration = 0;
        Position maxDurationPos = null;
        
        // 预热 (JVM Warm-up)
        System.out.println("Warming up...");
        for (int i = 0; i < 5; i++) {
            solver.solve(new Position(0, 0));
        }
        
        System.out.println("Starting benchmark...");
        long totalStart = System.currentTimeMillis();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position startPos = new Position(row, col);
                
                long start = System.nanoTime();
                Solution solution = solver.solve(startPos);
                long durationNs = System.nanoTime() - start;
                long durationMs = durationNs / 1_000_000;
                
                durations.add(durationMs);
                
                if (durationMs > maxDuration) {
                    maxDuration = durationMs;
                    maxDurationPos = startPos;
                }
                
                assertNotNull(solution, "Should find solution for " + startPos);
            }
        }
        
        long totalEnd = System.currentTimeMillis();
        
        // 统计
        double avgDuration = durations.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long totalDuration = totalEnd - totalStart;
        
        System.out.println("Benchmark Results:");
        System.out.println("Total Time (wall clock): " + totalDuration + " ms");
        System.out.println("Average Solving Time: " + String.format("%.2f", avgDuration) + " ms");
        System.out.println("Max Solving Time: " + maxDuration + " ms (at " + maxDurationPos + ")");
        System.out.println("Positions tested: " + durations.size());
        
        // 断言性能指标 (例如平均时间 < 50ms, 最大时间 < 500ms)
        // 注意：这些阈值取决于机器性能，这里设置得比较宽松
        assertTrue(avgDuration < 100, "Average solving time should be < 100ms");
        assertTrue(maxDuration < 1000, "Max solving time should be < 1000ms");
    }
}
