package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "新增课程请求参数")
public class CourseAddDTO {

    @ApiModelProperty(value = "课程标题", required = true, example = "Java编程入门")
    @NotBlank(message = "课程标题不能为空")
    @Length(min = 2, max = 100, message = "课程标题长度必须在2-100位之间")
    private String title;

    @ApiModelProperty(value = "课程封面URL", example = "https://xxx.com/cover.jpg")
    private String cover; // 表中允许为null，不做非空校验

    @ApiModelProperty(value = "分类ID", required = true, example = "1")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId; // 对应表中bigint类型

    @ApiModelProperty(value = "课程价格", example = "99.00")
    private BigDecimal price; // 表中允许为null，不做非空校验

    @ApiModelProperty(value = "课程描述", example = "本课程适合零基础学习者...")
    private String description;

    @ApiModelProperty(value = "课程开始时间", example = "2025-01-01 00:00:00")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "课程结束时间", example = "2025-02-01 00:00:00")
    private LocalDateTime endTime;
}