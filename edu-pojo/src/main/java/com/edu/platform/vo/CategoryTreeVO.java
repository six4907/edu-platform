package com.edu.platform.vo;

import lombok.Data;

import java.util.List;

/**
 * 分类树形结构VO（一级分类包含二级分类列表）
 */
@Data
public class CategoryTreeVO {
    private Long id;
    private String name;
    private Long parentId;
    private int sort;
    private List<CategoryTreeVO> children;
}