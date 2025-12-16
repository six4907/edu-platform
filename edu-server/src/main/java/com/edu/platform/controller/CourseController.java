package com.edu.platform.controller;

import com.edu.platform.dto.*;
import com.edu.platform.result.PageResult;
import com.edu.platform.result.Result;
import com.edu.platform.server.CourseService;
import com.edu.platform.vo.ChapterVO;
import com.edu.platform.vo.CourseDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/course")
@Slf4j
@Api(tags = "课程管理接口")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 新增课程
     * @param courseAddDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增课程（仅教师/管理员）")
    public Result<Void> addCourse(@Valid @RequestBody CourseAddDTO courseAddDTO) {
        log.info("接收新增课程请求：{}", courseAddDTO);
        courseService.addCourse(courseAddDTO);
        return Result.success();
    }

    /**
     * 修改课程
     * @param id
     * @param courseUpdateDTO
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改课程（仅课程创建者/管理员）")
    public Result updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseUpdateDTO courseUpdateDTO) {
        log.info("接收修改课程请求：id={}, 参数={}", id, courseUpdateDTO);
        courseService.updateCourse(id, courseUpdateDTO);
        return Result.success();
    }

    /**
     * 删除课程
     * @param id 课程ID
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除课程（仅课程创建者/管理员）")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        log.info("接收删除课程请求：id={}", id);
        courseService.deleteCourse(id);
        return Result.success();
    }

    /**
     * 获取课程详情（含章节和课时）
     * @param id 课程ID
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取课程详情（含章节和课时）")
    public Result<CourseDetailVO> getCourseDetail(@PathVariable Long id) {
        log.info("接收获取课程详情请求：id={}", id);
        CourseDetailVO coursedetailVO= courseService.getCourseDetail(id);
        return Result.success(coursedetailVO);
    }

    /**
     * 分页查询课程
     * @param coursePageQueryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询课程（公开接口）")
    public Result<PageResult> pageQuery(CoursePageQueryDTO coursePageQueryDTO) {
        log.info("接收课程分页查询请求：{}", coursePageQueryDTO);
        PageResult pageResult = courseService.pageQuery(coursePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据课程ID查询章节和小节
     * @param courseId 课程ID
     * @return 章节列表（包含小节）
     */
    @GetMapping("/chapter/{courseId}")
    @ApiOperation(value = "根据课程ID查询章节和小节")
    public Result<List<ChapterVO>> getChaptersWithVideos(@PathVariable Long courseId) {
        log.info("接收查询课程章节请求：courseId={}", courseId);
        List<ChapterVO> chapterVOList = courseService.getChaptersWithVideos(courseId);
        return Result.success(chapterVOList);
    }

    /**
     * 新增章节（仅课程创建者）
     * @param chapterAddDTO 章节信息
     * @return 操作结果
     */
    @PostMapping("/chapter")
    @ApiOperation(value = "新增章节（仅课程创建者可用）")
    public Result<Void> addChapter(@Valid @RequestBody ChapterAddDTO chapterAddDTO) {
        log.info("接收新增章节请求：{}", chapterAddDTO);
        courseService.addChapter(chapterAddDTO);
        return Result.success();
    }

    /**
     * 修改章节（仅课程创建者）
     * @param id 章节ID
     * @param chapterUpdateDTO 章节修改信息
     * @return 操作结果
     */
    @PutMapping("/chapter/{id}")
    @ApiOperation(value = "修改章节（仅课程创建者可用）")
    public Result<Void> updateChapter(
            @PathVariable Long id,
            @Valid @RequestBody ChapterUpdateDTO chapterUpdateDTO) {
        log.info("接收修改章节请求：id={}, 参数={}", id, chapterUpdateDTO);
        courseService.updateChapter(id, chapterUpdateDTO);
        return Result.success();
    }

    /**
     * 删除章节（仅课程创建者）
     * @param id 章节ID
     * @return 操作结果
     */
    @DeleteMapping("/chapter/{id}")
    @ApiOperation(value = "删除章节（仅课程创建者可用）")
    public Result<Void> deleteChapter(@PathVariable Long id) {
        log.info("接收删除章节请求：id={}", id);
        courseService.deleteChapter(id);
        return Result.success();
    }

    /**
     * 新增课时（仅课程创建者可用）
     * @param videoAddDTO 课时信息
     * @return 操作结果
     */
    @PostMapping("/video")
    @ApiOperation(value = "新增课时（仅课程创建者可用）")
    public Result<Void> addVideo(@Valid @RequestBody VideoAddDTO videoAddDTO) {
        log.info("接收新增课时请求：{}", videoAddDTO);
        courseService.addVideo(videoAddDTO);
        return Result.success();
    }

    /**
     * 修改课时（仅课程创建者可用）
     * @param id 课时ID
     * @param videoUpdateDTO 课时修改信息
     * @return 操作结果
     */
    @PutMapping("/video/{id}")
    @ApiOperation(value = "修改课时（仅课程创建者可用）")
    public Result<Void> updateVideo(
            @PathVariable Long id,
            @Valid @RequestBody VideoUpdateDTO videoUpdateDTO) {
        log.info("接收修改课时请求：id={}, 参数={}", id, videoUpdateDTO);
        courseService.updateVideo(id, videoUpdateDTO);
        return Result.success();
    }
}