package com.edu.platform.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Jackson 对象映射器（在线教育平台专用）
 * 功能：统一 Java 对象与 JSON 之间的序列化/反序列化规则
 * 核心适配：LocalDateTime/LocalDate/LocalTime 日期类型的格式统一，兼容未知属性，提升接口稳定性
 */
public class JacksonObjectMapper extends ObjectMapper {

    // ---------------------- 日期时间格式常量（适配教育平台场景） ----------------------
    /** 日期格式（如：2025-12-01）- 用于课程发布日期、报名日期等 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /** 日期时间格式（如：2025-12-01 19:30）- 用于课程创建时间、学习记录时间等（精确到分钟，满足业务需求） */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 时间格式（如：19:30:00）- 用于课时时长、直播开始时间等 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    // 支持多种日期时间格式
    private static final DateTimeFormatter[] SUPPORTED_FORMATTERS = {
            DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    };

    public JacksonObjectMapper() {
        super();

        // 1. 兼容处理：JSON中存在Java对象没有的属性时，不抛出异常（避免前端传参冗余导致接口报错）
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 2. 反序列化容错：忽略无法识别的属性（与上面配置互补，增强兼容性）
        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 3. 注册自定义模块：统一日期时间类型的序列化/反序列化规则
        SimpleModule simpleModule = new SimpleModule()
                // 反序列化：JSON字符串 → Java日期对象（支持指定格式，解析失败时抛明确异常）
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
                        DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)) {
                    @Override
                    public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException {
                        try {
                            return super.deserialize(p, ctxt);
                        } catch (DateTimeParseException e) {
                            throw new java.io.IOException("日期时间格式错误，需符合：" + DEFAULT_DATE_TIME_FORMAT, e);
                        }
                    }
                })
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(
                        DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)) {
                    @Override
                    public LocalDate deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException {
                        try {
                            return super.deserialize(p, ctxt);
                        } catch (DateTimeParseException e) {
                            throw new java.io.IOException("日期格式错误，需符合：" + DEFAULT_DATE_FORMAT, e);
                        }
                    }
                })
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(
                        DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)) {
                    @Override
                    public LocalTime deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException {
                        try {
                            return super.deserialize(p, ctxt);
                        } catch (DateTimeParseException e) {
                            throw new java.io.IOException("时间格式错误，需符合：" + DEFAULT_TIME_FORMAT, e);
                        }
                    }
                })
                // 序列化：Java日期对象 → JSON字符串（统一格式，前端直接展示）
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                        DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(
                        DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(
                        DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        // 注册模块到ObjectMapper
        this.registerModule(simpleModule);
    }
}
