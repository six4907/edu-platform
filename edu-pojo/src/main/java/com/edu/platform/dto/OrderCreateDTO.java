package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "创建支付订单请求参数")
public class OrderCreateDTO {
    @ApiModelProperty(value = "课程ID", required = true, example = "1")
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @ApiModelProperty(value = "支付类型：1-微信，2-支付宝", required = true, example = "1")
    @NotNull(message = "支付类型不能为空")
    private Integer payType;
}