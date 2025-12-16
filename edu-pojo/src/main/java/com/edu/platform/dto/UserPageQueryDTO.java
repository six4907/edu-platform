package com.edu.platform.dto;

import lombok.Data;

/**
 * 用户分页查询DTO（适配 UserMapper.pageQuery 方法）
 * 封装分页参数和筛选条件，用于管理员后台用户管理
 */
@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
public class UserPageQueryDTO {
    // 分页基础参数（前端传递）
    private int page; // 页码（从1开始）
    private int pageSize; // 每页条数（如10、20）

    // 筛选条件（可选，可为null）
    private String username; // 用户名（模糊查询）
    private Integer role; // 角色（1-学生，2-教师，3-管理员）
    private Integer status; // 状态（0-禁用，1-启用）
}