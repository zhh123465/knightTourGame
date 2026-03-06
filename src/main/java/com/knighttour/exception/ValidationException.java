package com.knighttour.exception;

/**
 * 输入验证异常
 * 
 * 当用户输入无效数据时抛出此异常。
 */
public class ValidationException extends KnightTourException {
    
    /**
     * 构造一个带消息的验证异常
     * 
     * @param message 异常消息
     */
    public ValidationException(String message) {
        super(message);
    }
    
    /**
     * 构造一个带消息和原因的验证异常
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
