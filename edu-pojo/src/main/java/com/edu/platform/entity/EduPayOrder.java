package com.edu.platform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单实体类（对应edu_pay_order表）
 */
@Data
public class EduPayOrder {
    private Long id;                  // 订单ID（主键）
    private String orderNo;           // 订单编号（唯一）
    private Long userId;              // 用户ID（关联edu_user表）
    private Long courseId;            // 课程ID（关联edu_course表）
    private BigDecimal totalFee;      // 支付金额（元）
    private Integer payType;          // 支付类型：1-微信，2-支付宝
    private Integer status;           // 订单状态：0-待支付，1-已支付，2-已取消，3-超时关闭
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime payTime;    // 支付时间
    private LocalDateTime updateTime; // 更新时间
}