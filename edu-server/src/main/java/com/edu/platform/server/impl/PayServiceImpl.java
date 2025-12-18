package com.edu.platform.server.impl;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.OrderCreateDTO;
import com.edu.platform.dto.OrderQueryDTO;
import com.edu.platform.dto.PayCreateDTO;
import com.edu.platform.entity.*;
import com.edu.platform.exception.BaseException;
import com.edu.platform.mapper.*;
import com.edu.platform.result.PageResult;
import com.edu.platform.server.PayService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 创建支付订单
     * @param orderCreateDTO
     * @return
     */
    @Transactional
    public String createOrder(OrderCreateDTO orderCreateDTO) {
        log.info("创建支付订单：{}", orderCreateDTO);
        Integer userRole = BaseContext.getUserRole();
        Long userId = BaseContext.getUserId();
        Long courseId = orderCreateDTO.getCourseId();

        // 1. 校验课程是否存在
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BaseException(MessageConstant.COURSE_NOT_FOUND);
        }

        // 定义用户关联ID（学生/教师ID）
        Long relatedUserId = null;
        // 先查询是否存在未支付订单（初始化为null）
        EduPayOrder unpaidOrder = null;

        // 2. 根据角色处理学生/教师信息，并查询未支付订单
        if (userRole == 1) { // 学生角色
            Student student = studentMapper.getById(userId);
            if (student == null) { // 增加学生信息校验
                throw new BaseException(MessageConstant.STUDENT_NOT_FOUND);
            }
            relatedUserId = student.getId();
            unpaidOrder = payOrderMapper.selectUnpaidByUserAndCourse(relatedUserId, courseId);
        } else if (userRole == 2) { // 教师角色
            Teacher teacher = teacherMapper.get(userId);
            if (teacher == null) { // 增加教师信息校验
                throw new BaseException(MessageConstant.TEACHER_NOT_FOUND);
            }
            relatedUserId = teacher.getId();
            unpaidOrder = payOrderMapper.selectUnpaidByUserAndCourse(relatedUserId, courseId);
        } else { // 不支持的角色
            throw new BaseException(MessageConstant.INVALID_USER_ROLE);
        }

        // 3. 校验是否已存在未支付订单，存在则直接返回订单号
        if (unpaidOrder != null) {
            log.warn("存在未支付订单：{}", unpaidOrder.getOrderNo());
            return unpaidOrder.getOrderNo();
        }

        // 4. 初始化订单对象（此时一定是新订单，不会为null）
        EduPayOrder order = new EduPayOrder();

        // 5. 生成订单编号（UUID简化）
        String orderNo = "ORDER_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        // 6. 封装订单信息
        order.setOrderNo(orderNo);
        order.setUserId(userId); // 统一设置用户关联ID
        order.setCourseId(courseId);
        order.setTotalFee(course.getPrice()); // 课程价格作为支付金额
        order.setPayType(orderCreateDTO.getPayType());
        order.setStatus(0); // 0-待支付
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 7. 保存订单
        payOrderMapper.insert(order);
        log.info("订单创建成功：{}", orderNo);
        return orderNo;
    }

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    @Transactional
    public void cancelOrder(String orderNo) {
        log.info("取消订单：{}", orderNo);
        Long userId = BaseContext.getUserId();

        // 1. 查询订单并校验权限
        EduPayOrder order = payOrderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (!order.getUserId().equals(userId)) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        // 2. 校验订单状态（仅待支付订单可取消）
        if (order.getStatus() != 0) {
            throw new BaseException("订单状态异常，无法取消");
        }

        // 3. 更新订单状态为“已取消”
        order.setStatus(2);
        order.setUpdateTime(LocalDateTime.now());
        payOrderMapper.updateStatus(order);
        log.info("订单取消成功：{}", orderNo);
    }

    /**
     * 分页查询用户订单
     * @param queryDTO
     * @return
     */
    public PageResult queryUserOrders(OrderQueryDTO queryDTO) {
        log.info("查询用户订单：{}", queryDTO);
        Long userId = BaseContext.getUserId();

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<EduPayOrder> page = payOrderMapper.pageQueryByUser(userId, queryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 创建支付
     * @param payCreateDTO
     * @return
     */
    public String createPay(PayCreateDTO payCreateDTO) {
        String orderNo = payCreateDTO.getOrderNo();
        Integer payType = payCreateDTO.getPayType();

        // 1. 校验订单状态（必须是“待支付”状态）
        EduPayOrder order = payOrderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != 0) { // 0-待支付
            throw new BaseException("订单状态异常，无法发起支付");
        }
        if(payType != order.getPayType()){
            throw new BaseException("支付方式不匹配");
        }

        // 2. 模拟支付逻辑（不调用真实第三方接口）
        String payInfo = "";
        if (payType == 1) { // 微信支付模拟
            payInfo = "模拟微信支付链接: http://mock.wechat.pay/" + orderNo;
        } else if (payType == 2) { // 支付宝支付模拟
            payInfo = "模拟支付宝支付链接: http://mock.alipay/" + orderNo;
        } else {
            throw new BaseException("不支持的支付方式");
        }

        // 3. 这里可以添加一个模拟的支付成功回调处理
        // 实际项目中这会由第三方支付平台异步调用
        simulatePaymentSuccess(orderNo);

        return payInfo;
    }

    // 添加模拟支付成功的方法
    private void simulatePaymentSuccess(String orderNo) {
        // 模拟1秒后支付成功（实际中是异步回调）
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                EduPayOrder order = payOrderMapper.selectByOrderNo(orderNo);
                if (order != null && order.getStatus() == 0) {
                    // 更新订单状态为已支付
                    order.setStatus(1); // 1-已支付
                    order.setUpdateTime(LocalDateTime.now());
                    payOrderMapper.updateStatus(order);

                    // 记录支付日志
                    EduPayLog payLog = new EduPayLog();
                    payLog.setOrderNo(orderNo);
                    payLog.setPayTime(LocalDateTime.now());
                    payLog.setPayPlatform(order.getPayType());
                    payLog.setTradeNo("MOCK_TRADE_" + System.currentTimeMillis());
                    payLog.setCreateTime(LocalDateTime.now());
                    payLogMapper.insert(payLog);

                    log.info("模拟支付成功，订单号: {}", orderNo);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("模拟支付处理中断", e);
            }
        }).start();
    }

    /**
     * 支付回调处理
     */
    @Transactional
    public void handlePayCallback(String orderNo, String tradeNo, Integer payPlatform, String callbackContent) {
        // 1. 查询订单
        EduPayOrder order = payOrderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 2. 更新订单状态为已支付
        order.setStatus(1); // 1-已支付
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        payOrderMapper.updateStatus(order);

        // 3. 记录支付日志（核心逻辑）
        EduPayLog payLog = new EduPayLog();
        payLog.setOrderNo(orderNo);
        payLog.setTradeNo(tradeNo);
        payLog.setPayPlatform(payPlatform);
        payLog.setCallbackContent(callbackContent);
        payLog.setCreateTime(LocalDateTime.now());
        payLogMapper.insert(payLog); // 调用日志Mapper插入记录

        log.info("支付日志记录成功：{}", orderNo);
    }

    public EduPayOrder getOrderByNo(String orderNo) {
        Long userId = BaseContext.getUserId();
        EduPayOrder order = payOrderMapper.selectByOrderNo(orderNo);

        // 验证订单归属
        if (order != null && !order.getUserId().equals(userId)) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        return order;
    }
}