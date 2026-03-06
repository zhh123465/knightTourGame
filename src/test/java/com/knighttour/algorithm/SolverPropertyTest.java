package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import com.knighttour.model.Solution;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.assertThat;

public class SolverPropertyTest {

    @Property(tries = 10) // 减少次数，因为求解比较慢
    void solutionCompleteness(@ForAll @IntRange(min = 0, max = 7) int row, 
                              @ForAll @IntRange(min = 0, max = 7) int col) {
        Board board = new Board();
        KnightTourSolver solver = new KnightTourSolver(board);
        solver.setAnimationDelay(0); // 禁用延迟
        
        Position startPos = new Position(row, col);
        Solution solution = solver.solve(startPos);
        
        assertThat(solution).isNotNull();
        assertThat(solution.isValid()).isTrue();
        
        // 验证路径长度
        assertThat(solution.getPath().size()).isEqualTo(63); // 63 moves for 64 cells
        
        // 验证矩阵完整性
        int[][] matrix = solution.getMatrix();
        boolean[] visited = new boolean[65]; // 1-64
        for (int[] r : matrix) {
            for (int val : r) {
                if (val >= 1 && val <= 64) {
                    visited[val] = true;
                }
            }
        }
        for (int i = 1; i <= 64; i++) {
            assertThat(visited[i]).isTrue();
        }
    }
}
