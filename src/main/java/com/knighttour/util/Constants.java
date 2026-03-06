package com.knighttour.util;

/**
 * 常量类
 * 
 * 定义系统中使用的全局常量。
 */
public final class Constants {
    
    /** 棋盘大小 */
    public static final int BOARD_SIZE = 8;
    
    /** 最大迭代次数（防止无限循环） */
    public static final int MAX_ITERATIONS = 10_000_000;
    
    /** 默认动画延迟（毫秒） */
    public static final int DEFAULT_ANIMATION_DELAY = 100;
    
    /** 骑士移动偏移量 */
    public static final int[][] MOVE_OFFSETS = {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    
    /** 私有构造函数防止实例化 */
    private Constants() {
        throw new AssertionError("No instances");
    }
}
