package com.edu.platform.constant;

/**
 * JWT令牌中存储的声明常量（Claims Key）
 */
public class JwtClaimsConstant {
    /**
     * 用户唯一ID（适配所有角色：学生/教师/管理员）
     */
    public static final String USER_ID = "userId";

    /**
     * 角色类型（1-学生，2-教师，3-管理员）
     */
    public static final String ROLE = "role";

    /**
     * 登录用户名（唯一标识，用于日志打印/快速识别）
     */
    public static final String USERNAME = "username";
}
