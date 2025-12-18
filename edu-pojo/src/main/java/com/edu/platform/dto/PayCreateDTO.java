package com.edu.platform.dto;

import lombok.Data;

@Data
public class PayCreateDTO {
    private String orderNo; // 订单号（必填）
    private Integer payType; // 支付方式（1-微信，2-支付宝，与订单中的payType一致）
}
