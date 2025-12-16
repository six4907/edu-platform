package com.edu.platform.exception;

/**
 * 业务异常基类（所有自定义业务异常都继承此类）
 * 作用：全局异常处理器可通过此类统一捕获所有业务异常，区分系统异常和业务异常
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}