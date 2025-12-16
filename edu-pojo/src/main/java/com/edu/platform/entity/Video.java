package com.edu.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    private Long id;
    private Long chapterId; // 修正为Long类型，与数据库bigint一致（原int改为Long）
    private String title;
    private String videoUrl; // 驼峰命名，与数据库字段video_url映射
    private String duration;
    private int sort;
    private Integer isFree; // 修正为Integer类型，匹配数据库tinyint（0-否，1-是）
    private LocalDateTime createTime; // 账号创建时间（自动记录）
    private LocalDateTime updateTime; // 账号信息最后更新时间（自动更新）
}