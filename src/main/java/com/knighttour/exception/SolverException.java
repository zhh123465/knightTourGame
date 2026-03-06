package com.knighttour.exception;

/**
 * 求解器异常
 * 
 * 当求解过程中发生错误时抛出此异常。
 */
public class SolverException extends KnightTourException {
    
    /**
     * 构造一个带消息的求解器异常
     * 
     * @param message 异常消息
     */
    public SolverException(String message) {
        super(message);
    }
    
    /**
     * 构造一个带消息和原因的求解器异常
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public SolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
