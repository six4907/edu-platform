package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "课程修改参数")
public class CourseUpdateDTO {

    @ApiModelProperty(value = "课程封面", required = true)
    @NotBlank(message = "课程封面不能为空")
    private String cover;

    @ApiModelProperty(value = "课程标题", required = true)
    @NotBlank(message = "课程标题不能为空")
    private String title;

    @ApiModelProperty(value = "课程价格", required = true)
    @NotNull(message = "课程价格不能为空")
    @PositiveOrZero(message = "课程价格不能为负数")
    private BigDecimal price;

    @ApiModelProperty(value = "课程描述", required = true)
    @NotBlank(message = "课程描述不能为空")
    private String description;

    @ApiModelProperty(value = "课程状态（0-草稿，1-发布）", required = true)
    @NotNull(message = "课程状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "课程开始时间", required = true)
    @NotNull(message = "课程开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "课程结束时间", required = true)
    @NotNull(message = "课程结束时间不能为空")
    private LocalDateTime endTime;
}