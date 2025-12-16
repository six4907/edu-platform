package com.edu.platform.constant;

/**
 * 密码常量
 */
public class PasswordConstant {

    /**
     * 新用户默认初始密码（管理员创建用户时使用）
     */
    public static final String DEFAULT_INIT_PASSWORD = "123456";

    /**
     * 密码最小长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 6;

    /**
     * 密码最大长度限制
     */
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 密码复杂度正则表达式（至少包含字母+数字，可选特殊字符）
     * 说明：^(?=.*[A-Za-z])(?=.*\d).{6,20}$
     * - (?=.*[A-Za-z])：必须包含至少一个字母（大小写均可）
     * - (?=.*\d)：必须包含至少一个数字
     * - .{6,20}：长度6-20位
     */
    public static final String PASSWORD_COMPLEXITY_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{6,20}$";

    /**
     * 密码复杂度提示文案
     */
    public static final String PASSWORD_COMPLEXITY_MSG = "密码长度需6-20位，且至少包含1个字母和1个数字";
}
