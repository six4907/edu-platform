package com.edu.platform.server;

import com.edu.platform.dto.*;
import com.edu.platform.entity.Course;
import com.edu.platform.result.PageResult;
import com.edu.platform.vo.ChapterVO;
import com.edu.platform.vo.CourseDetailVO;

import java.util.List;

public interface CourseService {

    /**
     * 新增课程
     * @param courseAddDTO 课程信息
     * @return
     */
    void addCourse(CourseAddDTO courseAddDTO);

    /**
     * 修改课程
     * @param id 课程ID
     * @param courseUpdateDTO 课程修改信息
     * @return
     */
    void updateCourse(Long id, CourseUpdateDTO courseUpdateDTO);

    /**
     * 删除课程
     * @param id 课程ID
     */
    void deleteCourse(Long id);

    /**
     * 获取课程详情
     * @param id 课程ID
     * @return
     */
    CourseDetailVO getCourseDetail(Long id);

    /**
     * 分页查询课程
     * @param coursePageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult pageQuery(CoursePageQueryDTO coursePageQueryDTO);

    /**
     * 根据课程ID查询章节和小节
     * @param courseId 课程ID
     * @return 章节列表（包含小节）
     */
    List<ChapterVO> getChaptersWithVideos(Long courseId);

    /**
     * 新增章节
     * @param chapterAddDTO 章节信息
     */
    void addChapter(ChapterAddDTO chapterAddDTO);

    /**
     * 修改章节
     * @param id 章节ID
     * @param chapterUpdateDTO 章节修改信息
     */
    void updateChapter(Long id, ChapterUpdateDTO chapterUpdateDTO);

    /**
     * 删除章节
     * @param id 章节ID
     */
    void deleteChapter(Long id);

    /**
     * 新增课时（仅课程创建者可操作）
     * @param videoAddDTO 课时信息
     */
    void addVideo(VideoAddDTO videoAddDTO);

    /**
     * 修改课时（仅课程创建者可操作）
     * @param id 课时ID
     * @param videoUpdateDTO 课时修改信息
     */
    void updateVideo(Long id, VideoUpdateDTO videoUpdateDTO);

    /**
     * 查询已发布课程
     */
    List<Course> findPublishedCourses();
}