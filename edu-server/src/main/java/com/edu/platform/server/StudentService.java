package com.edu.platform.server;

import com.edu.platform.dto.StudentUpdateDTO;
import com.edu.platform.entity.Course;
import com.edu.platform.entity.Enroll;
import com.edu.platform.result.PageResult;
import com.edu.platform.vo.StudentInfoVO;

public interface StudentService {
    /**
     * 修改学生信息
     * @param studentUpdateDTO
     */
  void update(StudentUpdateDTO studentUpdateDTO);

  /**
   * 获取学生信息
   * @param id
   * @return
   */
  StudentInfoVO getInfo(Long id);

  /**
   * 选课
   * @param courseId
   * @return
   */
  void select(Long courseId);

  /**
   * 退课
   * @param courseId
   * @return
   */
  void quitCourse(Long courseId);

    /**
     * 分页查询学生已选课程
     * @param studentId 学生ID
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult getSelectedCourses(Long studentId, int pageNum, int pageSize);
}
