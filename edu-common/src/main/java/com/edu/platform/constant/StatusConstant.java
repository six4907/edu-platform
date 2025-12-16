package com.edu.platform.constant;

/**
 * 状态常量类（在线教育平台专用）
 * 说明：所有状态采用 Integer 类型，0=禁用/未完成，1=启用/已完成，便于数据库存储和逻辑判断
 */
public class StatusConstant {
    // ---------------------- 通用状态（适用于所有模块） ----------------------
    /** 启用（通用：用户、分类、课程等） */
    public static final Integer ENABLE = 1;
    /** 禁用（通用：用户、分类、课程等） */
    public static final Integer DISABLE = 0;

    // ---------------------- 课程相关状态 ----------------------
    /** 课程状态：草稿（未发布） */
    public static final Integer COURSE_DRAFT = 0;
    /** 课程状态：已发布（可报名） */
    public static final Integer COURSE_PUBLISHED = 1;
    /** 课程状态：已下架（不可报名） */
    public static final Integer COURSE_OFFLINE = 2;

    // ---------------------- 章节/课时相关状态 ----------------------
    /** 课时状态：免费 */
    public static final Integer VIDEO_FREE = 1;
    /** 课时状态：付费（需选课/购买后观看） */
    public static final Integer VIDEO_PAID = 0;

    // ---------------------- 学习相关状态 ----------------------
    /** 选课状态：正常（已选课，可学习） */
    public static final Integer ENROLL_NORMAL = 1;
    /** 选课状态：已退课（不可学习） */
    public static final Integer ENROLL_CANCELLED = 0;
    /** 学习状态：未完成 */
    public static final Integer LEARN_UNFINISHED = 0;
    /** 学习状态：已完成 */
    public static final Integer LEARN_FINISHED = 1;
}
