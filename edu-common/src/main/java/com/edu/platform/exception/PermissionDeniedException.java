package com.edu.platform.exception;

/**
 * 权限不足异常（如非管理员执行管理员操作）
 */
public class PermissionDeniedException extends BaseException {

    public PermissionDeniedException(String message) {
        super(message);
    }
}
