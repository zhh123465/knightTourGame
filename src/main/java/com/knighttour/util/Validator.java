package com.knighttour.util;

import com.knighttour.exception.ValidationException;
import com.knighttour.model.Board;
import com.knighttour.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证工具类
 * 
 * 提供各种输入验证和参数检查功能。
 */
public class Validator {
    
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);
    
    /** 私有构造函数防止实例化 */
    private Validator() {}
    
    /**
     * 验证位置坐标是否有效
     * 
     * @param row 行坐标
     * @param col 列坐标
     * @return 如果坐标在 [0,7] 范围内返回 true，否则返回 false
     */
    public static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE;
    }
    
    /**
     * 解析位置字符串
     * 
     * @param input 格式为 "row,col" 的字符串
     * @return 解析后的 Position 对象
     * @throws ValidationException 如果格式错误或坐标无效
     */
    public static Position parsePosition(String input) throws ValidationException {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException("输入不能为空");
        }
        
        String[] parts = input.split(",");
        if (parts.length != 2) {
            throw new ValidationException("格式错误：请使用 'row,col' 格式，例如 '0,0'");
        }
        
        try {
            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());
            
            if (!isValidPosition(row, col)) {
                throw new ValidationException("坐标无效：行和列必须在 0 到 7 之间");
            }
            
            return new Position(row, col);
            
        } catch (NumberFormatException e) {
            throw new ValidationException("格式错误：坐标必须是整数");
        }
    }
    
    /**
     * 验证并放置骑士（模拟放置，实际只是验证位置是否为空且有效）
     * 
     * @param board 棋盘对象
     * @param pos 位置对象
     * @throws ValidationException 如果位置无效或已被占用
     */
    public static void validateAndPlace(Board board, Position pos) throws ValidationException {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        if (pos == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        
        if (!board.isValidPosition(pos.getRow(), pos.getCol())) {
            throw new ValidationException("位置无效：" + pos);
        }
        
        if (board.isVisited(pos.getRow(), pos.getCol())) {
            throw new ValidationException("位置已被占用：" + pos);
        }
        
        logger.debug("Position validated: {}", pos);
    }
}
