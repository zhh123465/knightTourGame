package com.knighttour.exception;

/**
 * 棋盘状态异常
 * 
 * 当棋盘状态不一致或损坏时抛出此异常。
 */
public class BoardStateException extends KnightTourException {
    
    /**
     * 构造一个带消息的棋盘状态异常
     * 
     * @param message 异常消息
     */
    public BoardStateException(String message) {
        super(message);
    }
    
    /**
     * 构造一个带消息和原因的棋盘状态异常
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public BoardStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
