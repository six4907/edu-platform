package com.edu.platform.mapper;

import com.edu.platform.entity.Course;
import com.edu.platform.entity.Enroll;
import com.edu.platform.entity.Student;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface StudentMapper {
    /**
     * 修改学生信息
     * @param student
     * @param id
     * edu_student表
     */
    @Update("update edu_student set introduction = #{student.introduction},school = #{student.school},grade = #{student.grade} where user_id = #{id}")
    void updateStudent(Student student,Long id);

    /**
     * 修改学生信息
     * @param student
     * @param id
     * edu_user表
     */
    @Update("update edu_user set nickname = #{student.nickName},avatar = #{student.avatar} where id = #{id}")
    void updateUser(Student student, Long id);
    /**
     * 根据id查询学生信息
     * @param id
     * @return edu_user实体
     */
    @Select("select * from edu_user where id = #{id}")
    Student getInfo(Long id);

    /**
     * 根据id查询学生信息
     * @param id
     * @return edu_student实体
     */
    @Select("select * from edu_student where user_id = #{id}")
    Student getById(Long id);

    /**
     * 选课
     * @param studentId,courseId,enrollTime
     * @return 选课记录
     */
    @Insert("insert into edu_enroll (student_id,course_id,enroll_time) values (#{studentId},#{courseId},#{enrollTime})")
    int select( Long studentId, Long courseId, LocalDateTime enrollTime);

    /**
     * 根据学生ID和课程ID查询选课记录（用于退课前校验）
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 选课记录
     */
    @Select("SELECT * FROM edu_enroll WHERE student_id = #{studentId} AND course_id = #{courseId} AND status = 1")
    Enroll selectByStudentIdAndCourseId( Long studentId,  Long courseId);

    /**
     * 单条退课：更新选课记录状态为“已退课”（逻辑删除，推荐）
     * @param studentId 学生ID
     * @param courseId 课程ID
     */
    @Update("UPDATE edu_enroll SET status = 0, update_time = NOW() WHERE student_id = #{studentId} AND course_id = #{courseId} AND status = 1")
    int updateStatusToQuit( Long studentId,  Long courseId);

    /**
     * 分页查询学生已选课程
     * @param studentId 学生ID
     * @return 课程分页数据
     */
    Page<Course> selectSelectedCourses(@Param("studentId") Long studentId);


}
