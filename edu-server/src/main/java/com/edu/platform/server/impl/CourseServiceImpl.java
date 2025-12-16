package com.edu.platform.server.impl;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.constant.StatusConstant;
import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.*;
import com.edu.platform.entity.Chapter;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Teacher;
import com.edu.platform.entity.Video;
import com.edu.platform.exception.BaseException;
import com.edu.platform.exception.PermissionDeniedException;
import com.edu.platform.mapper.CategoryMapper;
import com.edu.platform.mapper.CourseMapper;
import com.edu.platform.mapper.TeacherMapper;
import com.edu.platform.result.PageResult;
import com.edu.platform.server.CourseService;
import com.edu.platform.server.UserService;
import com.edu.platform.vo.ChapterVO;
import com.edu.platform.vo.CourseDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 新增课程
     *
     * @param courseAddDTO
     */
    @Transactional
    public void addCourse(@RequestBody CourseAddDTO courseAddDTO) {
        log.info("新增课程：{}", courseAddDTO);

        // 1. 权限校验（仅教师/管理员）
        Integer role = BaseContext.getUserRole();
        if (role == null || role < 2) { // 假设2=教师，3=管理员
            log.warn("新增课程失败：权限不足，角色={}", role);
            throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
        }

        // 2. 校验分类存在性
        Long categoryId = courseAddDTO.getCategoryId();
        if (categoryMapper.selectById(categoryId) == null) {
            log.warn("分类不存在：categoryId={}", categoryId);
            throw new IllegalArgumentException(MessageConstant.CATEGORY_NOT_FOUND);
        }

        // 3. 校验教师存在性（使用MessageConstant中用户相关的USER_NOT_FOUND）
        Long teacherId = BaseContext.getUserId();
        if (teacherMapper.selectById(teacherId) == null) {
            log.warn("教师不存在：teacherId={}", teacherId);
            throw new IllegalArgumentException(MessageConstant.USER_NOT_FOUND);
        }

        // 4. 校验课程标题唯一性（避免重复创建）
        String title = courseAddDTO.getTitle();
        // 假设存在查询方法：根据标题查询课程
        Course existingCourse = courseMapper.selectByTitle(title);
        if (existingCourse != null) {
            log.warn("课程标题已存在：title={}", title);
            throw new IllegalArgumentException(MessageConstant.COURSE_ALREADY_EXISTS);
        }

         Teacher teacher =teacherMapper.get(teacherId);
        // 5. 转换为实体并设置默认值
        Course course = new Course();
        BeanUtils.copyProperties(courseAddDTO, course);
        course.setTeacherId(teacher.getId());
        course.setStatus(StatusConstant.COURSE_DRAFT); // 0-草稿
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        course.setStartTime(courseAddDTO.getStartTime());
        course.setEndTime(courseAddDTO.getEndTime());

        // 6. 保存课程
        courseMapper.insert(course);
        log.info("新增课程成功，课程ID={}", course.getId());

    }

    /**
     * 修改课程
     *
     * @param id
     * @param courseUpdateDTO
     * @return
     */
    @Transactional
    public void updateCourse(Long id, @RequestBody CourseUpdateDTO courseUpdateDTO) {
        log.info("修改课程：id={}, 参数={}", id, courseUpdateDTO);
        Course course=new Course();
        Integer role = BaseContext.getUserRole();
        if (role == null || role < 2) { // 2=教师，3=管理员
            log.warn("新增课程失败：权限不足，角色={}", role);
            throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
        }
        BeanUtils.copyProperties(courseUpdateDTO, course);
        course.setId(id);
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.updateCourse(course);
        log.info("修改课程成功：id={}", id);

    }

    /**
     * 删除课程
     * @param id 课程ID
     */
    @Transactional
    public void deleteCourse(Long id) {
        log.info("删除课程：id={}", id);

        // 1. 权限校验（仅教师/管理员）
        Integer role = BaseContext.getUserRole();
        if (role == null || role < 2) { // 2=教师，3=管理员
            log.warn("删除课程失败：权限不足，角色={}", role);
            throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
        }

        // 2. 校验课程存在性
        Course course = courseMapper.selectById(id);
        if (course == null) {
            log.warn("删除课程失败：课程不存在，id={}", id);
            throw new IllegalArgumentException(MessageConstant.COURSE_NOT_FOUND);
        }

        // 3. 校验课程状态（已发布课程不能删除）
        if (course.getStatus() == StatusConstant.COURSE_PUBLISHED) {
            log.warn("删除课程失败：课程已发布，id={}", id);
            throw new IllegalArgumentException(MessageConstant.COURSE_ON_SALE);
        }

        // 4. 权限细化校验（非管理员只能删除自己创建的课程）
        if (role == 2) { // 教师角色
            Long currentUserId = BaseContext.getUserId();
            Teacher teacher = teacherMapper.get(currentUserId);
            if (!course.getTeacherId().equals(teacher.getId())) {
                log.warn("删除课程失败：非课程创建者，courseId={}, teacherId={}, currentUserId={}",
                        id, course.getTeacherId(), currentUserId);
                throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
            }
        }

        // 5. 执行删除
        courseMapper.deleteById(id);
        log.info("删除课程成功：id={}", id);
    }

    /**
     * 获取课程详情（含章节和课时）
     * @param id 课程ID
     * @return 课程详情VO
     */
    public CourseDetailVO getCourseDetail(Long id) {
        log.info("查询课程详情：id={}", id);

        // 1. 查询课程基本信息
        Course course = courseMapper.selectById(id);
        if (course == null) {
            log.warn("查询课程详情失败：课程不存在，id={}", id);
            throw new IllegalArgumentException(MessageConstant.COURSE_NOT_FOUND);
        }

        // 2. 查询课程包含的章节
        List<Chapter> chapterList = courseMapper.selectChaptersByCourseId(id);
        if (chapterList != null && !chapterList.isEmpty()) {
            // 3. 为每个章节查询关联的课时
            for (Chapter chapter : chapterList) {
                List<Video> videoList = courseMapper.selectByChapterId(chapter.getId());
                chapter.setVideoList(videoList);
            }
        }

        // 4. 封装返回VO
        CourseDetailVO courseDetailVO = new CourseDetailVO();
        courseDetailVO.setCourse(course);
        courseDetailVO.setChapterList(chapterList);

        log.info("查询课程详情成功：id={}，章节数={}", id, chapterList.size());
        return courseDetailVO;
    }

    /**
     * 分页查询课程
     * @param coursePageQueryDTO 分页查询参数
     * @return 分页结果
     */
    public PageResult pageQuery(CoursePageQueryDTO coursePageQueryDTO) {
        log.info("课程分页查询：{}", coursePageQueryDTO);

        // 开启分页
        PageHelper.startPage(coursePageQueryDTO.getPageNum(), coursePageQueryDTO.getPageSize());

        // 执行查询（需要在mapper中新增对应方法）
        Page<Course> page = courseMapper.pageQuery(coursePageQueryDTO);

        // 封装分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据课程ID查询章节和小节
     * @param courseId 课程ID
     * @return 章节列表
     */
    public List<ChapterVO> getChaptersWithVideos(Long courseId) {
        log.info("查询课程章节和小节：courseId={}", courseId);

        // 1. 查询章节列表
        List<Chapter> chapterList = courseMapper.selectByCourseId(courseId);

        // 2. 转换为VO并查询每个章节的小节
        List<ChapterVO> chapterVOList = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(chapter, chapterVO);

            // 3. 查询当前章节的小节
            List<Video> videoList = courseMapper.selectByChapterId(chapter.getId());
            chapterVO.setVideoList(videoList);

            chapterVOList.add(chapterVO);
        }

        return chapterVOList;
    }

    /**
     * 新增章节（仅课程创建者可操作）
     * @param chapterAddDTO 章节信息
     */
    @Transactional
    public void addChapter(ChapterAddDTO chapterAddDTO) {
        log.info("新增章节：{}", chapterAddDTO);

        // 1. 获取当前登录用户
        Long currentUserId = BaseContext.getUserId();
        Teacher teacher = teacherMapper.get(currentUserId);

        // 2. 验证课程是否存在且当前用户为课程创建者
        Long courseId = chapterAddDTO.getCourseId();
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BaseException(MessageConstant.COURSE_NOT_FOUND);
        }
        if (!teacher.getId().equals(course.getTeacherId())) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        // 3. 转换为实体并设置时间
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterAddDTO, chapter);
        chapter.setCreateTime(LocalDateTime.now());
        chapter.setUpdateTime(LocalDateTime.now());

        // 4. 保存章节
        courseMapper.insertChapter(chapter);
    }

    /**
     * 修改章节（仅课程创建者可操作）
     * @param id 章节ID
     */
    @Transactional
    public void updateChapter(Long id, ChapterUpdateDTO chapterUpdateDTO) {
        log.info("修改章节：id={}, 参数={}", id, chapterUpdateDTO);

        // 1. 获取当前登录用户
        Long currentUserId = BaseContext.getUserId();

        // 2. 验证章节是否存在
        Chapter chapter = courseMapper.selectChapterById(id);
        if (chapter == null) {
            throw new BaseException(MessageConstant.CHAPTER_NOT_FOUND);
        }

        // 3. 验证当前用户是否为课程创建者
        Long courseId = chapter.getCourseId();
        Teacher teacher = teacherMapper.get(currentUserId);
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BaseException(MessageConstant.COURSE_NOT_FOUND);
        }
        if (!teacher.getId().equals(course.getTeacherId())) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        // 4. 更新章节信息
        Chapter updateChapter = new Chapter();
        BeanUtils.copyProperties(chapterUpdateDTO, updateChapter);
        updateChapter.setId(id);
        updateChapter.setCourseId(courseId); // 保持原课程ID不变
        updateChapter.setUpdateTime(LocalDateTime.now());

        courseMapper.updateChapter(updateChapter);
    }

    /**
     * 删除章节（仅课程创建者可操作）
     * @param id 章节ID
     */
    @Transactional
    public void deleteChapter(Long id) {
        log.info("删除章节：id={}", id);

        // 1. 获取当前登录用户
        Long currentUserId = BaseContext.getUserId();
        Teacher teacher = teacherMapper.get(currentUserId);

        // 2. 验证章节是否存在
        Chapter chapter = courseMapper.selectChapterById(id);
        if (chapter == null) {
            throw new BaseException(MessageConstant.CHAPTER_NOT_FOUND);
        }

        // 3. 验证当前用户是否为课程创建者
        Long courseId = chapter.getCourseId();
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BaseException(MessageConstant.COURSE_NOT_FOUND);
        }
        if (!teacher.getId().equals(course.getTeacherId())) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        // 4. 检查章节下是否有关联课时
        List<Video> videoList = courseMapper.selectByChapterId(id);
        if (videoList != null && !videoList.isEmpty()) {
            throw new BaseException("章节下包含课时，无法直接删除");
        }

        // 5. 执行删除
        courseMapper.deleteByChapterId(id);
    }

    /**
     * 新增课时（仅课程创建者可操作）
     * @param videoAddDTO 课时信息
     */
    @Transactional
    public void addVideo(VideoAddDTO videoAddDTO) {
        log.info("新增课时：{}", videoAddDTO);

        // 1. 获取当前登录用户
        Long currentUserId = BaseContext.getUserId();
        Teacher teacher = teacherMapper.get(currentUserId);

        // 2. 验证章节是否存在
        Long chapterId = videoAddDTO.getChapterId();
        Chapter chapter = courseMapper.selectChapterById(chapterId);
        if (chapter == null) {
            throw new BaseException(MessageConstant.CHAPTER_NOT_FOUND);
        }

        // 3. 验证当前用户是否为课程创建者
        Long courseId = chapter.getCourseId();
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BaseException(MessageConstant.COURSE_NOT_FOUND);
        }
        if (!teacher.getId().equals(course.getTeacherId())) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        // 4. 转换为实体并设置默认值
        Video video = new Video();
        BeanUtils.copyProperties(videoAddDTO, video);
        // 计算排序号（当前章节最大排序+1）
        List<Video> videoList = courseMapper.selectByChapterId(chapterId);
        video.setSort(videoList.size() + 1);
        video.setCreateTime(LocalDateTime.now());
        video.setUpdateTime(LocalDateTime.now());

        // 5. 保存课时
        courseMapper.insertVideo(video);
        log.info("新增课时成功，课时ID={}", video.getId());
    }

    /**
     * 修改课时（仅课程创建者可操作）
     * @param id 课时ID
     */
    @Transactional
    public void updateVideo(Long id, VideoUpdateDTO videoUpdateDTO) {
        log.info("修改课时：id={}, 参数={}", id, videoUpdateDTO);

        // 1. 获取当前登录用户
        Long currentUserId = BaseContext.getUserId();
        Teacher teacher = teacherMapper.get(currentUserId);

        // 2. 验证课时是否存在
        Video video = courseMapper.selectVideoById(id);
        if (video == null) {
            throw new BaseException(MessageConstant.VIDEO_NOT_FOUND);
        }

        // 3. 通过章节ID查询课程信息
        Long chapterId = video.getChapterId();
        Chapter chapter = courseMapper.selectChapterById(chapterId);
        if (chapter == null) {
            throw new BaseException(MessageConstant.CHAPTER_NOT_FOUND);
        }

        // 4. 验证当前用户是否为课程创建者
        Long courseId = chapter.getCourseId();
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BaseException(MessageConstant.COURSE_NOT_FOUND);
        }
        if (!teacher.getId().equals(course.getTeacherId())) {
            throw new BaseException(MessageConstant.PERMISSION_DENIED);
        }

        // 5. 转换为实体并更新
        Video updateVideo = new Video();
        BeanUtils.copyProperties(videoUpdateDTO, updateVideo);
        updateVideo.setId(id);
        updateVideo.setChapterId(chapterId); // 保留原章节ID
        updateVideo.setUpdateTime(LocalDateTime.now());

        // 6. 执行更新
        courseMapper.updateVideo(updateVideo);
        log.info("修改课时成功：id={}", id);
    }

    /**
     * 查询已发布的课程列表
     */
    public List<Course> findPublishedCourses() {
        // 查询状态为“已发布”（假设1代表已发布）的课程
        return courseMapper.selectPublishedCourses();
    }
}