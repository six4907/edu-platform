package com.edu.platform.mapper;

import com.edu.platform.dto.OrderQueryDTO;
import com.edu.platform.entity.EduPayOrder;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PayOrderMapper {

    /**
     * 创建订单
     */
    @Insert("insert into edu_pay_order (" +
            "order_no, user_id, course_id, total_fee, pay_type, status, create_time, update_time" +
            ") values (" +
            "#{orderNo}, #{userId}, #{courseId}, #{totalFee}, #{payType}, #{status}, #{createTime}, #{updateTime}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(EduPayOrder order);

    /**
     * 根据订单编号查询
     */
    @Select("select * from edu_pay_order where order_no = #{orderNo}")
    EduPayOrder selectByOrderNo(String orderNo);

    /**
     * 根据用户ID和课程ID查询未支付订单
     */
    @Select("select * from edu_pay_order where user_id = #{userId} and course_id = #{courseId} and status = 0")
    EduPayOrder selectUnpaidByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 更新订单状态
     */
    @Update("update edu_pay_order set " +
            "status = #{status}, " +
            "pay_time = #{payTime}, " +
            "update_time = #{updateTime} " +
            "where order_no = #{orderNo}")
    void updateStatus(EduPayOrder order);

    /**
     * 分页查询用户订单
     */
    Page<EduPayOrder> pageQueryByUser(@Param("userId") Long userId, @Param("queryDTO") OrderQueryDTO queryDTO);
}