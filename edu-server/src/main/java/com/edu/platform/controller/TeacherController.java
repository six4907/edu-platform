package com.edu.platform.controller;

import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.TeacherUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Teacher;
import com.edu.platform.result.Result;
import com.edu.platform.server.TeacherService;
import com.edu.platform.vo.TeacherInfoVO;
import com.edu.platform.vo.TeacherVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@Slf4j
@Api(tags = "教师角色相关接口")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @PutMapping("/update")
    @ApiOperation(value = "信息修改") // 接口描述改为通用“信息修改”
    public Result<TeacherVO> update(@Valid @RequestBody TeacherUpdateDTO teacherUpdateDTO) {
        log.info("用户修改：{}", teacherUpdateDTO);
        Long userId = BaseContext.getUserId();

        TeacherVO teacherVO = teacherService.update(teacherUpdateDTO, userId);

        return Result.success("修改成功", teacherVO); // 个性化成功提示
    }


    @GetMapping("/getInfo")
    @ApiOperation(value = "信息获取")
    public Result<TeacherInfoVO> getInfo(){
        log.info("教师信息获取");
        long userId = BaseContext.getUserId();
        TeacherInfoVO teacherInfoVO=teacherService.get(userId);
        return Result.success("获取成功",teacherInfoVO);
    }

    @GetMapping("/course")
    @ApiOperation(value = "教师课程获取")
    public Result<List<Course>> getCourse(){
        long userid = BaseContext.getUserId();
        List<Course> courselist=teacherService.getCourse(userid);
        return Result.success("获取成功",  courselist);
    }
}
