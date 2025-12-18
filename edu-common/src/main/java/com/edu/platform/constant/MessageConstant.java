package com.edu.platform.constant;

/**
 * 信息提示常量类（在线教育平台专用）
 */
public class MessageConstant {    // ---------------------- 通用提示 ----------------------
    public static final String OPERATION_SUCCESS = "操作成功";
    public static final String OPERATION_FAILED = "操作失败";
    public static final String UNKNOWN_ERROR = "未知错误";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String PARAM_ERROR = "参数错误";

    // ---------------------- 用户相关 ----------------------
    public static final String USER_NOT_FOUND = "用户不存在";
    public static final String USER_ALREADY_EXISTS = "用户名已存在";
    public static final String PHONE_ALREADY_BOUND = "手机号已绑定其他账号";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String PASSWORD_EDIT_FAILED = "密码修改失败";
    public static final String PASSWORD_CONFIRM_NOT_MATCH = "两次密码输入不一致";
    public static final String USER_LOCKED = "账号被锁定，请联系管理员";
    public static final String USER_NOT_LOGIN = "用户未登录，请先登录";
    public static final String LOGIN_FAILED = "登录失败，请检查账号或密码";
    public static final String ROLE_ERROR = "角色权限错误";
    public static final String PERMISSION_DENIED = "权限不足，无法执行此操作";

    // ---------------------- 课程相关 ----------------------
    public static final String COURSE_NOT_FOUND = "课程不存在";
    public static final String COURSE_ALREADY_EXISTS = "课程名称已存在";
    public static final String COURSE_STATUS_ERROR = "课程状态错误";
    public static final String COURSE_ON_SALE = "已发布的课程不能删除";
    public static final String CHAPTER_NOT_FOUND = "章节不存在";
    public static final String VIDEO_NOT_FOUND = "课时视频不存在";

    // ---------------------- 分类相关 ----------------------
    public static final String CATEGORY_NOT_FOUND = "分类不存在";
    public static final String CATEGORY_ALREADY_EXISTS = "分类名称已存在";
    public static final String CATEGORY_BE_RELATED_BY_COURSE = "当前分类关联了课程，无法删除";
    public static final String CATEGORY_HAS_CHILDREN = "当前分类包含子分类，无法删除";

    // ---------------------- 学习相关 ----------------------
    public static final String ENROLL_ALREADY_EXISTS = "已选该课程，无需重复选课";
    public static final String ENROLL_NOT_FOUND = "未查询到选课记录";
    public static final String LEARN_RECORD_NOT_FOUND = "未查询到学习记录";

    // ---------------------- 订单相关 ----------------------
    public static final String ORDER_NOT_FOUND = "订单不存在";
    public static final String ORDER_ALREADY_EXISTS = "订单已存在";

    // ---------------------- 学生/教师相关 ----------------------
    public static final String STUDENT_NOT_FOUND = "学生信息不存在"; // 新增：学生信息不存在
    public static final String TEACHER_NOT_FOUND = "教师信息不存在"; // 新增：教师信息不存在
    public static final String INVALID_USER_ROLE = "不支持的用户角色"; // 新增：无效角色
}
