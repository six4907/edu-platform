package com.edu.platform.vo;

import com.edu.platform.entity.Video;
import lombok.Data;

import java.util.List;

/**
 * 章节VO（包含小节列表）
 */
@Data
public class ChapterVO {
    private Long id;
    private Long courseId;
    private String title;
    private int sort;
    private List<Video> videoList; // 小节列表
}