package com.edu.platform.vo;

import lombok.Data;

/**
 * 分类基础VO（用于二级分类展示）
 */
@Data
public class CategoryVO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
}