package com.edu.platform.mapper;

import com.edu.platform.entity.EduPayLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PayLogMapper {

    /**
     * 新增支付日志
     */
    @Insert("insert into edu_pay_log (" +
            "order_no, trade_no, pay_platform, callback_content, create_time" +
            ") values (" +
            "#{orderNo}, #{tradeNo}, #{payPlatform}, #{callbackContent}, #{createTime}" +
            ")")
    void insert(EduPayLog log);

    /**
     * 根据订单编号查询日志
     */
    @Select("select * from edu_pay_log where order_no = #{orderNo}")
    EduPayLog selectByOrderNo(String orderNo);
}