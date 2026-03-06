package com.knighttour.exception;

/**
 * 配置异常
 * 
 * 当配置文件读取或保存失败时抛出此异常。
 */
public class ConfigException extends KnightTourException {
    
    /**
     * 构造一个带消息的配置异常
     * 
     * @param message 异常消息
     */
    public ConfigException(String message) {
        super(message);
    }
    
    /**
     * 构造一个带消息和原因的配置异常
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
