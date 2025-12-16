package com.edu.platform.server.impl;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.constant.StatusConstant;
import com.edu.platform.context.BaseContext;
import com.edu.platform.dto.*;
import com.edu.platform.entity.User;
import com.edu.platform.exception.*;
import com.edu.platform.mapper.UserMapper;
import com.edu.platform.result.PageResult;
import com.edu.platform.server.UserService;
import com.edu.platform.vo.UserLoginVO;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

/**
 * 用户服务实现类（在线教育平台）
 * 处理用户登录、注册核心逻辑，适配多角色（学生/教师/管理员）
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Transactional
    public User login(@RequestBody UserLoginDTO userLoginDTO) {
        Long id = userLoginDTO.getId();
        String password = userLoginDTO.getPassword();
        log.info("用户登录请求：用户名={}", id);

        User user = userMapper.getById(id);
        if (user == null) {
            log.warn("用户登录失败：用户名={}，原因=用户不存在", id);
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            log.warn("用户登录失败：ID={}，原因=密码错误", id);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (user.getStatus().equals(StatusConstant.DISABLE)) {
            log.warn("用户登录失败：用户名={}，原因=账号被锁定", id);
            throw new AccountLockedException(MessageConstant.USER_LOCKED);
        }

        log.info("用户登录成功：用户名={}，角色={}", id, user.getRole());
        return user;
    }

    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    @Transactional
    public UserLoginVO register(@RequestBody  UserRegisterDTO userRegisterDTO) {
        log.info("用户注册请求：{}", userRegisterDTO);

        // 用户名查重
        User existUserByUsername = userMapper.getByUserNickName(userRegisterDTO.getNickname());
        if (existUserByUsername != null) {
            log.warn("用户注册失败：该昵称={}已存在", userRegisterDTO.getNickname());
            throw new DataAlreadyExistsException(MessageConstant.USER_ALREADY_EXISTS);
        }

        // 手机号查重
        User existUserByPhone = userMapper.getByPhone(userRegisterDTO.getPhone());
        if (existUserByPhone != null) {
            log.warn("用户注册失败：手机号={}已绑定", userRegisterDTO.getPhone());
            throw new DataAlreadyExistsException(MessageConstant.PHONE_ALREADY_BOUND);
        }

        // 密码加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes());

        // 构建 User 实体（补充 createUser/updateUser 赋值，避免数据库字段为 null）
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .password(encryptedPassword)
                .phone(userRegisterDTO.getPhone())
                .nickname(userRegisterDTO.getNickname())
                .avatar(userRegisterDTO.getAvatar())
                // 3. 关键：角色默认值（防止前端未传时为 null）
                .role(userRegisterDTO.getRole() == null ? 1 : userRegisterDTO.getRole())
                .status(StatusConstant.ENABLE)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 插入数据库
        userMapper.insert(user);
        log.info("用户注册成功：用户名={}，用户ID={}", user.getUsername(), user.getId());

        //插入学生的数据
        if (userRegisterDTO.getRole() == 1) {
            userMapper.insertStudent(user.getId(), userRegisterDTO.getUsername());
            log.info("edu_student表插入数据成功");
        }
        // 插入教师的数据
        if (userRegisterDTO.getRole() == 2) {
            userMapper.insertTeacher(user.getId(), userRegisterDTO.getUsername());
            log.info("edu_teacher表插入数据成功");
        }

        user=userMapper.getByUserNickName(userRegisterDTO.getNickname());
        // 构建响应 VO
        return UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public UserLoginVO getCurrentUser() {
        // 1. 从上下文获取当前用户ID（由JWT拦截器提前存入）
        Long userId = BaseContext.getUserId();
        if (userId == null) {
            log.warn("获取当前用户信息失败：用户未登录");
            throw new AccountNotFoundException(MessageConstant.USER_NOT_LOGIN);
        }

        // 2. 调用Mapper查询用户信息
        User user = userMapper.getById(userId);
        if (user == null) {
            log.warn("获取当前用户信息失败：用户不存在，ID={}", userId);
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        // 3. 转换为VO并返回
        log.info("获取当前用户信息成功，ID={}", userId);
        return UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .build();
    }

    /**
     * 修改用户信息
     * @param userUpdateDTO
     * @return
     */
    public UserLoginVO updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("用户信息修改请求：{}", userUpdateDTO);

        // 1. 校验参数（至少修改一个字段）
        if (userUpdateDTO.getNickname() == null &&
                userUpdateDTO.getAvatar() == null &&
                userUpdateDTO.getEmail() == null) {
            log.warn("用户信息修改失败：未提交任何修改字段");
            throw new ParameterInvalidException("至少需要修改一个字段");
        }

        // 2. 获取当前登录用户ID
        Long userId = BaseContext.getUserId();
        if (userId == null) {
            log.warn("用户信息修改失败：用户未登录");
            throw new AccountNotFoundException(MessageConstant.USER_NOT_LOGIN);
        }

        // 3. 查询用户信息（校验用户存在）
        User user = userMapper.getById(userId);
        if (user == null) {
            log.warn("用户信息修改失败：用户不存在，ID={}", userId);
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        // 4. 选择性更新字段（仅更新非null的参数）
        if (userUpdateDTO.getNickname() != null) {
            user.setNickname(userUpdateDTO.getNickname());
        }
        if (userUpdateDTO.getAvatar() != null) {
            user.setAvatar(userUpdateDTO.getAvatar());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        user.setUpdateTime(LocalDateTime.now()); // 更新时间戳

        // 5. 执行更新
        userMapper.update(user);
        log.info("用户信息修改成功：ID={}", userId);

        // 6. 返回更新后的用户信息（不含token，前端无需重新登录）
        return UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
    }

    /**
     * 修改密码
     * @param passwordUpdateDTO
     */
    public void updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        log.info("用户密码修改请求：{}", passwordUpdateDTO);

        // 1. 获取当前登录用户ID
        Long userId = BaseContext.getUserId();
        if (userId == null) {
            log.warn("密码修改失败：用户未登录");
            throw new AccountNotFoundException(MessageConstant.USER_NOT_LOGIN);
        }

        // 2. 查询用户信息（校验用户存在）
        User user = userMapper.getById(userId);
        if (user == null) {
            log.warn("密码修改失败：用户不存在，ID={}", userId);
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        // 3. 验证旧密码
        String encryptedOldPassword = DigestUtils.md5DigestAsHex(passwordUpdateDTO.getOldPassword().getBytes());
        if (!encryptedOldPassword.equals(user.getPassword())) {
            log.warn("密码修改失败：旧密码错误，用户ID={}", userId);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 4. 校验新旧密码是否一致
        if (passwordUpdateDTO.getOldPassword().equals(passwordUpdateDTO.getNewPassword())) {
            log.warn("密码修改失败：新旧密码相同，用户ID={}", userId);
            throw new ParameterInvalidException("新密码不能与旧密码相同");
        }

        // 5. 加密新密码并更新
        String encryptedNewPassword = DigestUtils.md5DigestAsHex(passwordUpdateDTO.getNewPassword().getBytes());
        userMapper.updatePassword(
                userId,
                encryptedNewPassword,
                LocalDateTime.now()
        );
        log.info("密码修改成功：用户ID={}", userId);
    }

    /**
     * 修改用户状态（禁用/启用）
     * @param id 用户ID
     * @param status 状态（0-禁用，1-启用）
     */
    public void updateStatus(Long id, Integer status) {
        log.info("修改用户状态：id={}，status={}", id, status);

        // 1. 校验当前登录用户是否为管理员
        Integer currentRole = BaseContext.getUserRole();
        if (currentRole == null || currentRole != 3) { // 3-管理员角色
            log.warn("修改用户状态失败：非管理员操作，当前角色={}", currentRole);
            throw new PermissionDeniedException(MessageConstant.ROLE_ERROR);
        }

        // 2. 校验用户是否存在
        User user = userMapper.getById(id);
        if (user == null) {
            log.warn("修改用户状态失败：用户不存在，id={}", id);
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        // 3. 校验状态参数合法性
        if (!status.equals(StatusConstant.ENABLE) && !status.equals(StatusConstant.DISABLE)) {
            log.warn("修改用户状态失败：无效状态值，status={}", status);
            throw new ParameterInvalidException(MessageConstant.PARAM_ERROR);
        }

        // 4. 执行状态更新
        userMapper.updateStatus(
                id,
                status,
                LocalDateTime.now(),
                user.getUsername() // 更新人ID为当前管理员
        );
        log.info("用户状态修改成功：id={}，新状态={}", id, status);
    }

    /**
     * 分页查询用户（仅管理员）
     * @param userPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    public PageResult pageQuery(@RequestBody UserPageQueryDTO userPageQueryDTO) {
        log.info("分页查询用户：{}", userPageQueryDTO);

        // 1. 校验当前登录用户是否为管理员
        Integer currentRole = BaseContext.getUserRole();
        if (currentRole == null || currentRole != 3) { // 3-管理员角色
            log.warn("分页查询用户失败：非管理员操作，当前角色={}", currentRole);
            throw new PermissionDeniedException(MessageConstant.ROLE_ERROR);
        }

        // 2. 执行分页查询（PageHelper自动分页）
        Page<User> page = userMapper.pageQuery(userPageQueryDTO);

        // 3. 封装分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }
}