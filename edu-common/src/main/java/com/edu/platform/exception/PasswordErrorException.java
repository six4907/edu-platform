package com.edu.platform.exception;

/**
 * 密码错误异常（登录时密码比对失败）
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException() {
        super();
    }

    public PasswordErrorException(String message) {
        super(message);
    }
}