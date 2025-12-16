package com.edu.platform.mapper;

import com.github.pagehelper.Page;
import com.edu.platform.dto.UserPageQueryDTO;
import com.edu.platform.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

/**
 * 用户Mapper接口（遵循EmployeeMapper风格：注解+XML混合、PageHelper分页）
 */
@Mapper
public interface UserMapper {

    /**
     * 根据昵称查询用户（登录、用户名查重用）
     * @param userNickName 昵称（唯一）
     * @return 用户实体
     */
    @Select("select * from edu_user where nickname = #{userNickName}")
    User getByUserNickName(String userNickName);

    /**
     * 根据手机号查询用户（手机号查重、找回密码用）
     * @param phone 手机号（唯一）
     * @return 用户实体
     */
    @Select("select * from edu_user where phone = #{phone}")
    User getByPhone(String phone);

    /**
     * 新增用户（注册、管理员创建用户用）
     * @param user 用户实体
     */
    @Insert("insert into edu_user (username, password, phone, nickname, avatar, role, status, create_time, update_time) " +
            "values " +
            "(#{username}, #{password}, #{phone}, #{nickname}, #{avatar}, #{role}, #{status}, #{createTime}, #{updateTime})")
    // 关键：添加注解返回自增ID
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(User user);

    /**
     * 分页查询（管理员后台用户管理用）
     * @param userPageQueryDTO 分页查询条件（页码、每页条数、用户名模糊搜、角色、状态）
     * @return PageHelper分页结果
     */
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

    /**
     * 根据主键动态修改用户属性（更新昵称、头像、手机号等）
     * @param user 用户实体（携带id和要修改的字段）
     */
    void update(User user);

    /**
     * 根据id查询用户信息
     * @param id 用户ID
     * @return 用户实体
     */
    @Select("select * from edu_user where id = #{id}")
    User getById(Long id);

    /**
     * 更新用户密码（修改密码、重置密码用）
     * @param id 用户ID
     * @param newPassword
     */
    @Update("update edu_user set password = #{newPassword}, update_time = #{updateTime} where id = #{id}")
    void updatePassword(Long id, String newPassword, LocalDateTime updateTime);

    /**
     * 更新用户状态（启用/禁用用）
     * @param id 用户ID
     * @param status 状态（0-禁用，1-启用）
     * @param updateTime 更新时间
     * @param updateUser 更新人ID
     */
    @Update("update edu_user set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateStatus(Long id, Integer status, LocalDateTime updateTime, String updateUser);

    /**
     * 将学生信息导入edu_student表
     * @param userId, realName
     */
    @Insert("insert into edu_student (user_id, real_name) values (#{userId},#{realName})")
    void insertStudent( Long userId,  String realName);

    /**
     * 将教师信息导入edu_teacher表
     * @param userId,realName
     */
    @Insert("insert into edu_teacher (user_id, real_name) values (#{userId},#{realName})")
    void insertTeacher( Long userId,  String realName);
}