package com.edu.platform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程实体类（对应edu_course表）
 */
@Data
public class Course {

    private Long id;          //课程ID（主键，bigint类型）

    private String title;     //课程标题（varchar(100)，非空）

    private String cover;     //封面URL（varchar(255)，可为空）

    private Long categoryId;  //分类ID（bigint，非空）

    private Long teacherId;   //教师ID（bigint，非空，关联edu_teacher表）

    private BigDecimal price;  //课程价格（decimal(10,2)，可为空）

    private String description; //课程描述（text，可为空）

    private Integer status;   //状态：0-草稿，1-已发布（tinyint，可为空）

    private LocalDateTime startTime;   // 课程开始时间

    private LocalDateTime endTime;     // 课程结束时间

    private LocalDateTime createTime;   //创建时间（datetime，可为空）

    private LocalDateTime updateTime; //更新时间（datetime，可为空）

}