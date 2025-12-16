package com.edu.platform.handler;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.exception.BaseException;
import com.edu.platform.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器（优先捕获自定义业务异常）
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 优先捕获所有自定义业务异常（BaseException子类）
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> baseExceptionHandler(BaseException ex) {
        log.error("业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("，"));
        log.error("参数校验异常：{}", errorMsg);
        return Result.error(errorMsg);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<Void> sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        log.error("SQL约束异常：{}", message);
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String duplicateValue = split[2];
            if (message.contains("uk_username")) {
                return Result.error("用户名「" + duplicateValue + "」已存在");
            } else if (message.contains("uk_phone")) {
                return Result.error("手机号「" + duplicateValue + "」已绑定");
            } else {
                return Result.error("该数据已存在，无法重复添加");
            }
        } else if (message.contains("foreign key constraint fails")) {
            return Result.error("当前数据已关联其他记录，无法删除或修改");
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleJsonParseError(HttpMessageNotReadableException e) {
        log.warn("JSON解析失败：", e);
        String msg = "用户ID必须为数字，请检查参数格式";
        return Result.error(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> exceptionHandler(Exception ex) {
        log.error("系统未知异常：", ex);
        return Result.error("系统繁忙，请稍后再试");
    }

}