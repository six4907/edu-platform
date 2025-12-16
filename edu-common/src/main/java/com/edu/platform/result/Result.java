package com.edu.platform.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果（在线教育平台专用）
 * @param <T> 响应数据类型
 */
@Data
@ApiModel(description = "统一返回结果") // Swagger 文档说明
public class Result<T> implements Serializable {

    @ApiModelProperty(value = "响应状态码（200=成功，500=失败）", example = "200")
    private Integer code; // 编码：200成功，500失败（对齐HTTP状态码，更易理解）

    @ApiModelProperty(value = "响应信息（成功/失败提示）", example = "操作成功")
    private String msg; // 错误/成功信息

    @ApiModelProperty(value = "响应数据（成功时返回，失败时为null）")
    private T data; // 响应数据

    // ---------------------- 成功响应（兼容原有调用，新增默认提示） ----------------------
    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功"; // 新增默认成功提示，前端无需额外处理
        return result;
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功"; // 新增默认成功提示
        result.data = data;
        return result;
    }

    /**
     * 扩展：成功响应（自定义提示+数据）
     * 场景：如“课程创建成功”“选课成功”等个性化提示
     */
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = msg;
        result.data = data;
        return result;
    }

    // ---------------------- 失败响应 ----------------------
    /**
     * 失败响应（自定义提示）
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 500; // 原0改为500，对齐HTTP状态码，前端更易识别
        result.msg = msg;
        return result;
    }

    /**
     * 扩展：失败响应（自定义状态码+提示）
     * 场景：如401未登录、403无权限等特殊错误
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
