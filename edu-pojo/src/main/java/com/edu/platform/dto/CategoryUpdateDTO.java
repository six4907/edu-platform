package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "分类修改请求参数")
public class CategoryUpdateDTO {

    @ApiModelProperty(value = "分类名称", required = true, example = "编程语言")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty(value = "排序号", required = true, example = "2")
    @NotNull(message = "排序号不能为空")
    private Integer sort;
}