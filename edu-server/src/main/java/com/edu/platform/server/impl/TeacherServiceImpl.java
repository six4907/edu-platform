package com.edu.platform.server.impl;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.TeacherUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Teacher;
import com.edu.platform.exception.AccountNotFoundException;
import com.edu.platform.exception.ParameterInvalidException;
import com.edu.platform.mapper.TeacherMapper;
import com.edu.platform.server.TeacherService;
import com.edu.platform.vo.TeacherInfoVO;
import com.edu.platform.vo.TeacherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    LocalDateTime updateTime = LocalDateTime.now();

    /**
     * 教师信息修改
     * @param teacherUpdateDTO
     * @return
     */
    @Transactional
    public TeacherVO update(@RequestBody TeacherUpdateDTO teacherUpdateDTO, long id) {
        log.info("用户信息修改请求：{}", teacherUpdateDTO);

        // 1. 校验参数（至少修改一个字段）
        if (teacherUpdateDTO.getRealName() == null && teacherUpdateDTO.getTitle() == null && teacherUpdateDTO.getIntroduction() == null && teacherUpdateDTO.getTeachingYears() == null) {
            log.warn("用户信息修改失败：未提交任何修改字段");
            throw new ParameterInvalidException("至少需要修改一个字段");
        }

        // 2. 获取当前登录用户ID
        Long userId = BaseContext.getUserId();

        // 3. 查询用户信息（校验用户存在）
        Teacher teacher = teacherMapper.get(userId);
        if (teacher == null) {
            log.warn("用户信息修改失败：用户不存在，ID={}", userId);
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        // 4. 选择性更新字段（仅更新非null的参数）
        if (teacherUpdateDTO.getTitle() != null) {
            teacher.setTitle(teacherUpdateDTO.getTitle());
        }
        if (teacherUpdateDTO.getIntroduction() != null) {
            teacher.setIntroduction(teacherUpdateDTO.getIntroduction());
        }
        if (teacherUpdateDTO.getTeachingYears() != null) {
            teacher.setTeachingYears(teacherUpdateDTO.getTeachingYears());
        }

        teacher.setUpdateTime(LocalDateTime.now());
        updateTime=teacher.getUpdateTime();
        // 5. 执行更新
        teacherMapper.updateTeacher(teacher, userId);

        if(teacherUpdateDTO.getNickName() != null){
            teacher.setNickName(teacherUpdateDTO.getNickName());
        }
        if(teacherUpdateDTO.getAvatar() != null){
            teacher.setAvatar(teacherUpdateDTO.getAvatar());
        }
        if(teacherUpdateDTO.getNickName() != null || teacherUpdateDTO.getAvatar() != null){
            teacherMapper.updateUser(teacher, userId);
        }
        log.info("用户信息修改成功：ID={}", userId);

        Teacher teacher1 = teacherMapper.getById(userId);
        return TeacherVO.builder()
                .realName(teacher.getRealName())
                .title(teacher.getTitle())
                .introduction(teacher.getIntroduction())
                .teachingYears(teacher.getTeachingYears())
                .nickName(teacher1.getNickName())
                .avatar(teacher1.getAvatar())
                .phone(String.valueOf(teacher1.getPhone()))
                .email(teacher1.getEmail())
                .build();
    }

    /**
     * 获取教师信息
     * @param id
     * @return
     */
    public TeacherInfoVO get(Long id) {
        log.info("教师信息获取");
        Teacher teacher1 = teacherMapper.getById(id);
        Teacher teacher2 = teacherMapper.get(id);
        TeacherInfoVO teacherInfoVO= new TeacherInfoVO().builder()
                .id(id)
                .realName(teacher2.getRealName())
                .nickName(teacher1.getNickName())
                .title(teacher2.getTitle())
                .introduction(teacher2.getIntroduction())
                .phone(String.valueOf(teacher1.getPhone()))
                .email(teacher1.getEmail())
                .teachingYears(teacher2.getTeachingYears())
                .createTime(teacher1.getCreateTime())
                .updateTime(updateTime)
                .build();
        return teacherInfoVO;
    }

    /**
     * 获取教师课程
     * @param id
     * @return
     */
    public List<Course> getCourse(long id) {
        log.info("教师课程获取");
        Teacher teacher = teacherMapper.get(id);
        List<Course>  course=teacherMapper.getCourse(teacher.getId());
        return course;
    }
}
