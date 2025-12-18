package com.edu.platform.controller;

import com.edu.platform.dto.OrderCreateDTO;
import com.edu.platform.dto.OrderQueryDTO;
import com.edu.platform.dto.PayCreateDTO;
import com.edu.platform.entity.EduPayOrder;
import com.edu.platform.result.PageResult;
import com.edu.platform.result.Result;
import com.edu.platform.server.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/pay")
@Api(tags = "支付管理接口")
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;

    @PostMapping("/order")
    @ApiOperation("创建支付订单")
    public Result<String> createOrder(@RequestBody @Valid OrderCreateDTO orderCreateDTO) {
        log.info("创建支付订单：{}", orderCreateDTO);
        String orderNo = payService.createOrder(orderCreateDTO);
        return Result.success(orderNo);
    }

    @PostMapping("/order/{orderNo}/cancel")
    @ApiOperation("取消订单")
    public Result cancelOrder(@PathVariable String orderNo) {
        log.info("取消订单：{}", orderNo);
        payService.cancelOrder(orderNo);
        return Result.success();
    }

    @GetMapping("/orders")
    @ApiOperation("分页查询用户订单")
    public Result<PageResult> queryUserOrders(OrderQueryDTO queryDTO) {
        log.info("查询用户订单：{}", queryDTO);
        PageResult pageResult = payService.queryUserOrders(queryDTO);
        return Result.success(pageResult);
    }


    /**
     * 发起支付
     * @param payCreateDTO
     * @return
     */
    @PostMapping("/create")
    @ApiOperation("发起支付")
    public Result<String> createPay(@RequestBody PayCreateDTO payCreateDTO) {
        log.info("发起支付：{}", payCreateDTO);
        String payInfo = payService.createPay(payCreateDTO);
        return Result.success(payInfo);
    }


    /**
     * 查询订单状态
     * @param orderNo
     * @return
     */
    @GetMapping("/order/{orderNo}/status")
    @ApiOperation("查询订单状态")
    public Result<EduPayOrder> getOrderStatus(@PathVariable String orderNo) {
        log.info("查询订单状态：{}", orderNo);
        EduPayOrder order = payService.getOrderByNo(orderNo);
        return Result.success(order);
    }
}