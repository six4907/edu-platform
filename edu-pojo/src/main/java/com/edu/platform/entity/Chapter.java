package com.edu.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    private Long id;
    private Long courseId; // 修正为Long类型，与数据库bigint一致（原int改为Long）
    private String title;
    private int sort;
    private List<Video> videoList; // 新增：章节包含的课时列表
    private LocalDateTime createTime; // 创建时间（自动记录）
    private LocalDateTime updateTime; // 最后更新时间（自动更新）
}