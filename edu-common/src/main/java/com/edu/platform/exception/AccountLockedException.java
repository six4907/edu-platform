package com.edu.platform.exception;

/**
 * 账号被锁定异常（用户状态为禁用）
 */
public class AccountLockedException extends BaseException {

    // 1. 无参构造方法（必须与类名一致）
    public AccountLockedException() {
        super();
    }

    // 2. 带错误信息的构造方法（必须与类名一致）
    public AccountLockedException(String message) {
        super(message);
    }


}