package com.edu.platform.server;

import com.edu.platform.dto.OrderCreateDTO;
import com.edu.platform.dto.OrderQueryDTO;
import com.edu.platform.dto.PayCreateDTO;
import com.edu.platform.entity.EduPayOrder;
import com.edu.platform.result.PageResult;

/**
 * 支付服务接口
 */
public interface PayService {

    /**
     * 创建支付订单
     * @param orderCreateDTO 创建订单参数
     * @return 订单编号
     */
    String createOrder(OrderCreateDTO orderCreateDTO);

    /**
     * 取消订单
     * @param orderNo 订单编号
     */
    void cancelOrder(String orderNo);

    /**
     * 分页查询用户订单
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageResult queryUserOrders(OrderQueryDTO queryDTO);

    /**
     * 发起支付（调用第三方支付接口）
     * @param payCreateDTO 支付参数（订单号、支付方式）
     * @return 支付链接/参数（前端用于调起支付页面）
     */
    String createPay(PayCreateDTO payCreateDTO);

    /**
     * 根据订单编号查询订单
     * @param orderNo 订单编号
     * @return 订单信息
     */
    EduPayOrder getOrderByNo(String orderNo);

}