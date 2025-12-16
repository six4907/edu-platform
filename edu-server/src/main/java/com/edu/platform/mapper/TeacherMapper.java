package com.edu.platform.mapper;

import com.edu.platform.dto.TeacherUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Teacher;
import com.edu.platform.vo.TeacherInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TeacherMapper {
    /**
     * 根据ID查询教师
     * @param id
     * @return
     */
    @Select("select * from edu_teacher where user_id = #{id}")
    Teacher selectById(Long id);

    /**
     * 修改教师信息（支持部分字段更新）
     * @param teacher,userId
     */
    void updateTeacher(Teacher teacher, Long userId);

    /**
     * 修改用户信息
     * @param teacher
     * @param id
     */
    void updateUser(Teacher teacher, Long id);

    /**
     * 根据ID查询教师
     * @param id
     * @return
     */
    @Select("select * from edu_teacher where user_id = #{id}")
    Teacher get(Long id);

    /**
     * 根据ID查询教师信息
     * @param id
     * @return
     */
    @Select("select * from edu_user where id = #{id}")
    Teacher getById(Long id);


    /**
     * 根据ID查询教师所教课程
     * @param id
     * @return
     */
    List<Course> getCourse(Long id);
}
