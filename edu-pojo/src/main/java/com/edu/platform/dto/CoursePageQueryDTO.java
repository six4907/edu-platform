package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "课程分页查询参数")
public class CoursePageQueryDTO {
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private int pageNum; // 页码（从1开始）

    @ApiModelProperty(value = "每页条数", required = true, example = "10")
    private int pageSize; // 每页记录数

    @ApiModelProperty(value = "分类ID（可选）", example = "1")
    private Long categoryId; // 分类筛选

    @ApiModelProperty(value = "课程标题（模糊查询，可选）", example = "Java")
    private String title; // 标题模糊搜索

    @ApiModelProperty(value = "课程状态（可选，0-草稿，1-已发布，2-已下架）", example = "1")
    private Integer status; // 状态筛选
}