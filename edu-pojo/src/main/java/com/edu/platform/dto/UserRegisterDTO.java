package com.edu.platform.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户注册请求DTO（接收前端注册参数）
 */
@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名长度必须在2-20位之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password; // 密码（前端明文，后端加密）

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone; // 手机号（唯一）

    @NotBlank(message = "昵称不能为空")
    @Length(min = 2, max = 20, message = "昵称长度必须在2-20位之间")
    private String nickname; // 昵称

    // 可选字段（前端可传可不传，有默认值）
    private String avatar; // 头像URL（默认null）
    private Integer role = 1; // 角色（默认1-学生，前端可指定2-教师，管理员只能后台创建）
}