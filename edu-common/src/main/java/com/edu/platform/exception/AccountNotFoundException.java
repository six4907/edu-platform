package com.edu.platform.exception;

/**
 * 账号不存在异常（登录时用户名未查询到）
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}