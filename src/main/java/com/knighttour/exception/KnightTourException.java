package com.knighttour.exception;

/**
 * 马踏棋盘游戏的基础异常类
 * 
 * 所有自定义异常都应继承此类。
 */
public class KnightTourException extends Exception {
    
    /**
     * 构造一个带消息的异常
     * 
     * @param message 异常消息
     */
    public KnightTourException(String message) {
        super(message);
    }
    
    /**
     * 构造一个带消息和原因的异常
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public KnightTourException(String message, Throwable cause) {
        super(message, cause);
    }
}
