package com.edu.platform.server;

import com.edu.platform.dto.TeacherUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Teacher;
import com.edu.platform.vo.TeacherInfoVO;
import com.edu.platform.vo.TeacherVO;

import java.util.List;

public interface TeacherService {
    /**
     * 教师信息更改
     * @param teacherUpdateDTO,id
     * @return
    */

    TeacherVO update(TeacherUpdateDTO teacherUpdateDTO, long id);

    /**
     * 教师信息获取
     * @param id
     * @return
     */
    TeacherInfoVO get(Long id);

    /**
     * 查询教师课程
     * @param id
     * @return
     */
    List<Course> getCourse(long id);
}
