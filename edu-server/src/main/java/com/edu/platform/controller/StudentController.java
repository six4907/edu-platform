package com.edu.platform.controller;

import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.StudentUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.result.PageResult;
import com.edu.platform.result.Result;
import com.edu.platform.server.CourseService;
import com.edu.platform.server.StudentService;
import com.edu.platform.vo.StudentInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@Slf4j
@Api(tags = "学生角色相关接口")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;


    /**
     * 完善学生信息
     * @param studentUpdateDTO
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "完善学生信息")
    public Result update(@Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        log.info("用户修改：{}", studentUpdateDTO);
        studentService.update(studentUpdateDTO);
        return Result.success("学生信息修改成功");
    }

    /**
     * 获取学生信息
     * @return
     */
    @GetMapping("/getStudentInfo")
    @ApiOperation(value = "学生信息获取")
    public Result<StudentInfoVO> getStudentInfo(){
        Long userId = BaseContext.getUserId();
        StudentInfoVO studentInfoVO=studentService.getInfo(userId);
        return Result.success("学生信息获取成功", studentInfoVO);
    }

    /**
     * 选课
     * @param courseId
     * @return
     */
    @PostMapping("/enroll/{courseId}")
    @ApiOperation(value = "选课")
    public Result select(@PathVariable Long courseId){
        log.info("用户选课：{}", courseId);
        studentService.select(courseId);
        return Result.success("选课成功");
    }

    /**
     * 退课
     * @param courseId
     * @return
     */
    @DeleteMapping("/enroll/{courseId}")
    @ApiOperation(value = "退课")
    public Result quitCourse(@PathVariable long courseId){
        studentService.quitCourse(courseId);
        return  Result.success("退课成功");
    }

    /**
     * 查询学生已选课程
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/course")
    @ApiOperation(value = "查询学生已选课程")
    public Result<PageResult> getSelectedCourses(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("学生查询已选课程：pageNum={}, pageSize={}", pageNum, pageSize);
        Long studentId = BaseContext.getUserId();
        PageResult pageResult = studentService.getSelectedCourses(studentId, pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 查询可选课程（供学生选择）
     * @return
     */
    @GetMapping("/available")
    @ApiOperation(value = "查询可选课程（供学生选择）")
    public Result<List<Course>> getAvailableCourses() {
        List<Course> courses = courseService.findPublishedCourses();
        return Result.success(courses);
    }
}
