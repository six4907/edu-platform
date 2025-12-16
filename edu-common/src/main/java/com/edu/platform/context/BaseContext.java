package com.edu.platform.context;

/**
 * 线程上下文工具类（存储当前请求的用户核心信息）
 * 用途：在同一请求线程中，快速获取登录用户的ID、角色等信息（无需反复传参）
 * 注意：使用后需移除，避免线程池复用导致内存泄漏
 */
public class BaseContext {

    /**
     * 存储当前登录用户的唯一ID（适配所有角色：学生/教师/管理员）
     */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * 存储当前登录用户的角色类型（1-学生，2-教师，3-管理员），用于权限控制
     */
    private static final ThreadLocal<Integer> USER_ROLE = new ThreadLocal<>();

    // ---------------------- 用户ID相关操作 ----------------------

    /**
     * 设置当前用户ID
     *
     * @param userId 用户唯一ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户唯一ID（未登录时返回null）
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    // ---------------------- 用户角色相关操作 ----------------------

    /**
     * 设置当前用户角色
     *
     * @param userRole 角色类型（1-学生，2-教师，3-管理员）
     */
    public static void setUserRole(Integer userRole) {
        USER_ROLE.set(userRole);
    }

    /**
     * 获取当前用户角色
     *
     * @return 角色类型（未登录时返回null）
     */
    public static Integer getUserRole() {
        return USER_ROLE.get();
    }

    // ---------------------- 清理上下文 ----------------------

    /**
     * 移除当前线程的所有上下文信息（必须在请求结束时调用，避免内存泄漏）
     * 建议：在拦截器的 postHandle 或 afterCompletion 中调用
     */
    public static void clear() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}