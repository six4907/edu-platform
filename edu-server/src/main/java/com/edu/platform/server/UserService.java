package com.edu.platform.server;

import com.edu.platform.dto.*;
import com.edu.platform.entity.User;
import com.edu.platform.result.PageResult;
import com.edu.platform.vo.UserLoginVO;


public interface UserService {
    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
    /**
     * 注册
     * @param userRegisterDTO
     * @return
     */
    UserLoginVO register(UserRegisterDTO userRegisterDTO);

    /**
     * 获取当前登录用户信息
     * @return 用户信息VO
     */
    UserLoginVO getCurrentUser();

    /**
     * 修改用户信息
     * @param userUpdateDTO
     */
    UserLoginVO updateUserInfo(UserUpdateDTO userUpdateDTO);

    /**
     * 修改密码
     * @param passwordUpdateDTO
     */
    void updatePassword(PasswordUpdateDTO passwordUpdateDTO);

    /**
     * 修改用户状态（禁用/启用）
     * @param id 用户ID
     * @param status 状态（0-禁用，1-启用）
     */
    void updateStatus(Long id, Integer status);

    /**
     * 分页查询用户
     * @param userPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);
}
