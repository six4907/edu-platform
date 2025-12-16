package com.edu.platform.mapper;

import com.edu.platform.dto.CoursePageQueryDTO;
import com.edu.platform.entity.Chapter;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Video;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    /**
     * 新增课程
     */
    @Insert("insert into edu_course (" +
            "title, cover, category_id, teacher_id, price, description, status, create_time, update_time,start_time,end_time" +
            ") values (" +
            "#{title}, #{cover}, #{categoryId}, #{teacherId}, #{price}, #{description}, #{status}, #{createTime}, #{updateTime},#{startTime},#{endTime}" +
            ")")
    void insert(Course course);

    /**
     * 根据标题查询课程（用于校验唯一性）
     * @param title
     * @return
     */
    @Select("select * from edu_course where title = #{title}")
    Course selectByTitle(String title);

    /**
     * 根据ID查询课程
     * @param id
     * @return
     */
    @Select("select * from edu_course where id = #{id}")
    Course selectById(Long id);

    /**
     * 更新课程信息
     * @param course
     */
    @Update("update edu_course set " +
            "cover = #{cover}, " +
            "title = #{title}, " +
            "price = #{price}, " +
            "description= #{description}, "+
            "status = #{status}, " +
            "start_time = #{startTime}, " +
            "update_time = #{updateTime}, " +
            "end_time= #{endTime} "+
            "where id = #{id}")
    void updateCourse(Course course);

    /**
     * 根据ID删除课程
     * @param id
     */
    @Delete("delete from edu_course where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据课程ID查询章节（按sort排序）
     * @param courseId
     */
    @Select("select id, course_id as courseId, title, sort from edu_chapter where course_id = #{courseId} order by sort")
    List<Chapter> selectByCourseId(Long courseId);

    /**
     * 根据章节ID查询课时（按sort排序）
     * @param chapterId 章节ID
     * @return 小节列表
     */
    @Select("select " +
            "id, " +
            "chapter_id as chapterId, " +
            "title, " +
            "video_url as videoUrl, " +
            "duration, " +
            "sort, " +
            "is_free as isFree "+
            "from edu_video " +
            "where chapter_id = #{chapterId} " +
            "order by sort")
    List<Video> selectByChapterId(Long chapterId);

    /**
     * 根据课程ID查询章节（按sort排序）
     */
    @Select("select id, course_id as courseId, title, sort from edu_chapter where course_id = #{courseId} order by sort")
    List<Chapter> selectChaptersByCourseId(Long courseId);

    /**
     * 分页查询课程
     * @param coursePageQueryDTO
     */
    Page<Course> pageQuery(CoursePageQueryDTO coursePageQueryDTO);

    /**
     * 新增章节
     * @param chapter
     */
    @Insert("insert into edu_chapter (course_id, title, sort, create_time, update_time) " +
            "values (#{courseId}, #{title}, #{sort}, #{createTime}, #{updateTime})")
    void insertChapter(Chapter chapter);

    /**
     * 根据ID查询章节
     * @param id
     * @return
     */
    @Select("select id, course_id as courseId, title, sort from edu_chapter where id = #{id}")
    Chapter selectChapterById(Long id);

    /**
     * 更新章节信息
     * @param chapter
     */
    @Update("update edu_chapter set " +
            "title = #{title}, " +
            "sort = #{sort}, " +
            "update_time = #{updateTime} " +
            "where id = #{id}")
    void updateChapter(Chapter chapter);

    /**
     * 根据ID删除章节
     * @param id
     */
    @Delete("delete from edu_chapter where id = #{id}")
    void deleteByChapterId(Long id);

    /**
     * 新增课时
     * @param video
     */
    @Insert("insert into edu_video (" +
            "chapter_id, title, video_url, duration, sort, is_free, create_time, update_time" +
            ") values (" +
            "#{chapterId}, #{title}, #{videoUrl}, #{duration}, #{sort}, #{isFree}, #{createTime}, #{updateTime}" +
            ")")
    void insertVideo(Video video);

    /**
     * 根据ID查询课时
     * @param id
     */
    @Select("select * from edu_video where id = #{id}")
    Video selectVideoById(Long id);

    /**
     * 根据章节ID查询课时数量（用于判断章节是否有课时）
     */
    @Select("select count(*) from edu_video where chapter_id = #{chapterId}")
    int countVideosByChapterId(Long chapterId);

    /**
     * 更新课时信息
     * @param video
     */
    @Update("update edu_video set " +
            "title = #{title}, " +
            "video_url = #{videoUrl}, " +
            "is_free = #{isFree}, " +
            "update_time = #{updateTime} " +
            "where id = #{id}")
    void updateVideo(Video video);

    /**
     * 根据分类ID查询课程数量
     * @param categoryId 分类ID
     */
    @Select("select count(*) from edu_course where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);

    /**
     * 查询所有已发布课程
     * @return
     */
    @Select("select id, title, cover, teacher_id , price, description , start_time ,update_time from edu_course where status = 1")
    List<Course> selectPublishedCourses();
}