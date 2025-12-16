package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "新增章节请求参数")
public class ChapterAddDTO {

    @ApiModelProperty(value = "课程ID", required = true, example = "1")
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @ApiModelProperty(value = "章节标题", required = true, example = "第一章：Java基础")
    @NotBlank(message = "章节标题不能为空")
    private String title;

    @ApiModelProperty(value = "排序号", required = true, example = "1")
    @NotNull(message = "排序号不能为空")
    private Integer sort;
}