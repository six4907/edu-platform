package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "新增分类请求参数")
public class CategoryAddDTO {

    @ApiModelProperty(value = "分类名称", required = true, example = "编程语言")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty(value = "父分类ID（0为一级分类）", required = true, example = "0")
    @NotNull(message = "父分类ID不能为空")
    private Long parentId;
}