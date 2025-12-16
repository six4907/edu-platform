package com.edu.platform.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;                  // 用户唯一标识ID
    private String username;          // 登录用户名（唯一不可重复）
    private String password;          // 加密后的登录密码（存储BCrypt/MD5加密结果）
    private String nickname;          // 用户昵称（显示用，可修改）
    private String avatar;            // 头像图片的URL地址
    private String phone;             // 绑定的手机号（唯一不可重复）
    private String email;             // 绑定的电子邮箱
    private Integer role;             // 角色类型：1-学生，2-教师，3-管理员
    private Integer status;           // 账号状态：0-禁用（无法登录），1-正常（可登录）
    private LocalDateTime createTime; // 账号创建时间（自动记录）
    private LocalDateTime updateTime; // 账号信息最后更新时间（自动更新）
}
