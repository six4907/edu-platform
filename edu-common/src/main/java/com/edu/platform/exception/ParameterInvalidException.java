package com.edu.platform.exception;

/**
 * 参数校验异常（如用户名为空、手机号格式错误等）
 */
public class ParameterInvalidException extends BaseException {

    public ParameterInvalidException() {
        super();
    }

    public ParameterInvalidException(String message) {
        super(message);
    }
}