package com.edu.platform.vo;

import com.edu.platform.entity.Chapter;
import com.edu.platform.entity.Course;
import lombok.Data;

import java.util.List;

/**
 * 课程详情VO（整合课程基本信息、章节、课时）
 * 用于前端展示完整的课程详情数据
 */
@Data
public class CourseDetailVO {
    /**
     * 课程基本信息
     */
    private Course course;

    /**
     * 章节列表（每个章节包含对应的课时列表）
     */
    private List<Chapter> chapterList;
}