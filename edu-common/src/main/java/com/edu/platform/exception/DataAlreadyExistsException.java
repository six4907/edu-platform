package com.edu.platform.exception;

/**
 * 数据已存在异常（如用户名、手机号重复）
 */
public class DataAlreadyExistsException extends BaseException {

    public DataAlreadyExistsException() {
        super();
    }

    public DataAlreadyExistsException(String message) {
        super(message);
    }
}