package com.edu.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付日志实体类（对应edu_pay_log表）
 */
@Data
public class EduPayLog {
    private Long id;                  // 日志ID（主键）
    private String orderNo;           // 订单编号（关联edu_pay_order表）
    private String tradeNo;           // 第三方支付交易号
    private Integer payPlatform;      // 支付平台：1-微信，2-支付宝
    private LocalDateTime payTime;    // 支付时间
    private String callbackContent;   // 支付回调内容
    private LocalDateTime createTime; // 创建时间
}