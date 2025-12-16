package com.edu.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*课程分类表*/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
}
