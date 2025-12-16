package com.edu.platform.server.impl;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.StudentUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Enroll;
import com.edu.platform.entity.Student;
import com.edu.platform.exception.ParameterInvalidException;
import com.edu.platform.mapper.CourseMapper;
import com.edu.platform.mapper.StudentMapper;
import com.edu.platform.result.PageResult;
import com.edu.platform.server.StudentService;
import com.edu.platform.vo.StudentInfoVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 完善学生信息
     * @param studentUpdateDTO
     */
    public void update(StudentUpdateDTO studentUpdateDTO) {
        log.info("用户信息修改请求：{}", studentUpdateDTO);
        Long id = BaseContext.getUserId();
        if (studentUpdateDTO.getSchool() == null && studentUpdateDTO.getGrade() == null && studentUpdateDTO.getIntroduction() == null
        && studentUpdateDTO.getNickName() == null && studentUpdateDTO.getAvatar()== null) {
            log.warn("用户信息修改失败：未修改任何信息");
            throw new ParameterInvalidException("至少需要修改一处信息");
        }
        Student student = new Student();

        // 选择性更新字段（仅更新非null的参数）
        if(studentUpdateDTO.getSchool() != null) {
            student.setSchool(studentUpdateDTO.getSchool());
        }
        if(studentUpdateDTO.getGrade() != null) {
            student.setGrade(studentUpdateDTO.getGrade());
        }
        if(studentUpdateDTO.getIntroduction() != null) {
            student.setIntroduction(studentUpdateDTO.getIntroduction());
        }
        studentMapper.updateStudent(student,id);

        if(studentUpdateDTO.getNickName() != null){
            student.setNickName(studentUpdateDTO.getNickName());
        }
        if(studentUpdateDTO.getAvatar() != null){
            student.setAvatar(studentUpdateDTO.getAvatar());
        }
        if(studentUpdateDTO.getNickName() != null || studentUpdateDTO.getAvatar() != null){
            studentMapper.updateUser(student,id);
        }
        log.info("用户信息修改成功：{}", studentUpdateDTO);
    }


    /**
     * 根据ID获取学生信息
     * @param id
     * @return
     */
    public StudentInfoVO getInfo(Long id) {
        log.info("学生信息获取");
        Student student1 = studentMapper.getInfo(id);
        Student student2 = studentMapper.getById(id);

        StudentInfoVO studentInfoVO = StudentInfoVO.builder()
                .id(id)
                .realName(student2.getRealName())
                .school(student2.getSchool())
                .grade(student2.getGrade())
                .nikeName(student1.getNickName())
                .avatar(student1.getAvatar())
                .phone(student1.getPhone())
                .email(student1.getEmail())
                .introduction(student2.getIntroduction())
                .createTime(student1.getCreateTime())
                .updateTime(student1.getUpdateTime())
                .build();
        return studentInfoVO;
    }

    /**
     * 选课
     * @param courseId
     * @return
     */
    public void  select(Long courseId) {
        // 生成选课时间（当前时间）
        LocalDateTime enrollTime = LocalDateTime.now();
        Long id = BaseContext.getUserId();
        Integer role = BaseContext.getUserRole(); // 需要在BaseContext中添加获取角色的方法

        // 校验角色是否为学生
        if (role != 1) {
            throw new RuntimeException("仅学生账号可进行选课操作");
        }

        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            log.warn("选课失败：课程不存在，courseId={}", courseId);
            throw new RuntimeException("课程不存在或已被删除");
        }

        // 校验学生ID是否存在于edu_student表
        Student student = studentMapper.getById(id);
        if (student == null) {
            throw new RuntimeException("学生信息不存在，无法选课");
        }
        Long studentId = student.getId();

        try {
            studentMapper.select(studentId, courseId, enrollTime);
            log.info("学生选课成功：studentId={}, courseId={}", id, courseId);
        } catch (DuplicateKeyException e) {
            // 捕获唯一约束异常（已选该课程）
            log.warn("选课失败：学生{}已选课程{}", id, courseId);
            throw new RuntimeException(MessageConstant.ENROLL_ALREADY_EXISTS);
        } catch (Exception e) {
            log.error("选课数据库操作失败", e);
            throw new RuntimeException(MessageConstant.OPERATION_FAILED);
        }
    }

    /**
     * 退课
     * @param courseId
     * @return
     */
    @Transactional
    public void quitCourse(Long courseId) {
        Long studentId = BaseContext.getUserId();

        // 1. 参数校验
        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException("学生ID和课程ID不能为空");
        }

        // 2. 校验学生是否选了该课程（且状态为已选课）
        Enroll enroll= studentMapper.selectByStudentIdAndCourseId(studentId, courseId);
        if (enroll == null) {
            throw new RuntimeException("未找到该学生的选课记录，无法退课");
        }

        // 3. 【可选】校验课程是否可退课（如课程已开始则不能退）
        // 这里可根据业务需求添加，比如查询课程表的状态/开始时间
         Course course = courseMapper.selectById(courseId);
         if (course.getStartTime().isBefore(LocalDateTime.now())) {
             throw new RuntimeException("课程已开始，无法退课");
         }

        // 4. 执行退课（更新状态）
        int affectedRows = studentMapper.updateStatusToQuit(studentId, courseId);
        if (affectedRows == 0) {
            throw new RuntimeException("退课失败，请重试");
        }
    }

    /**
     * 分页查询已选课程
     * @param studentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult getSelectedCourses(Long studentId, int pageNum, int pageSize) {
        log.info("分页查询学生已选课程：studentId={}, pageNum={}, pageSize={}", studentId, pageNum, pageSize);

        // 开启分页
        PageHelper.startPage(pageNum, pageSize);

        // 查询已选课程（关联课程表和教师表）
        Page<Course> page = studentMapper.selectSelectedCourses(studentId);

        List<Course> records = page.getResult();
        // 封装分页结果
        return new PageResult(page.getTotal(), records);
    }
}
