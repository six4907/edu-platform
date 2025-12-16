package com.edu.platform.controller;

import com.edu.platform.constant.JwtClaimsConstant;
import com.edu.platform.constant.StatusConstant;
import com.edu.platform.dto.*;
import com.edu.platform.result.PageResult;
import com.edu.platform.result.Result;
import com.edu.platform.utils.JwtUtil;
import com.edu.platform.entity.User;
import com.edu.platform.vo.UserLoginVO;
import com.edu.platform.server.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块接口（适配学生/教师/管理员多角色）
 * 说明：支持所有角色登录，通过返回的 role 字段区分权限
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Api(tags = "用户相关接口") // 标签改为通用“用户”，覆盖所有角色
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // 注入优化后的 JwtUtil（无需手动传秘钥/有效期）

    /**
     * 用户登录（适配学生/教师/管理员，无需按角色拆分接口）
     * @param userLoginDTO
     * @return 登录成功VO
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录") // 接口描述改为通用“用户登录”
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);

        // 1. 调用Service层登录校验（Service层已处理用户名不存在、密码错误等逻辑）
        User user = userService.login(userLoginDTO);

        // 2. 构建JWT存储的核心信息（用户ID+角色，用于后续权限控制）
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId().longValue()); // 统一用 USER_ID（适配所有角色）
        claims.put(JwtClaimsConstant.ROLE, user.getRole()); // 存储角色，前端用于菜单渲染
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername()); // 存储用户名，用于日志打印

        // 3. 生成JWT令牌（调用优化后的JwtUtil，无需手动传秘钥和有效期）
        String token = jwtUtil.createToken(claims);

        // 4. 构建返回VO（对齐优化后的UserLoginVO字段，修复原错误）
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername()) // 字段名统一为username（和实体类一致）
                .nickname(user.getNickname())
                .role(user.getRole()) // 补充角色字段，前端区分权限
                .avatar(user.getAvatar()) // 补充头像字段，前端直接展示
                .token(token)
                .build();

        log.info("用户登录成功，用户ID：{}，角色：{}", user.getId(), user.getRole());
        return Result.success("登录成功", userLoginVO); // 个性化成功提示
    }

    /**
     * 用户注册（适配学生/教师/管理员，无需按角色拆分接口）
     * @param userRegisterDTO
     * @return 包含令牌的VO
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<UserLoginVO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        // 调用Service注册，返回包含令牌的VO
        UserLoginVO userLoginVO = userService.register(userRegisterDTO);
        return Result.success("注册成功", userLoginVO);
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    @GetMapping("/current")
    @ApiOperation(value = "获取当前登录用户信息")
    public Result<UserLoginVO> getCurrentUser() {
        log.info("获取当前登录用户信息");
        // 调用Service层获取用户信息（Service层会处理未登录等异常）
        UserLoginVO userLoginVO = userService.getCurrentUser();
        return Result.success(userLoginVO);
    }

    /**
     * 修改用户信息（支持部分字段更新）
     * @param userUpdateDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改用户信息（昵称、头像、邮箱）")
    public Result<UserLoginVO> updateUserInfo(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("修改用户信息：{}", userUpdateDTO);
        UserLoginVO userLoginVO = userService.updateUserInfo(userUpdateDTO);
        return Result.success("信息修改成功", userLoginVO);
    }

    /**
     * 修改密码
     * @param passwordUpdateDTO
     * @return
     */
    @PutMapping("/password")
    @ApiOperation(value = "修改密码")
    public Result updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        log.info("修改密码：{}", passwordUpdateDTO);
        userService.updatePassword(passwordUpdateDTO);
        return Result.success("密码修改成功，请重新登录");
    }

    /**
     * 禁用/启用用户（仅管理员）
     * @param id 用户ID
     * @param status 状态（0-禁用，1-启用）
     * @return 操作结果
     */
    @PutMapping("/status/{id}/{status}")
    @ApiOperation(value = "禁用/启用用户")
    public Result<String> updateStatus(@PathVariable Long id,@PathVariable Integer status) {
        log.info("修改用户状态：id={}，status={}", id, status);
        userService.updateStatus(id, status);
        return Result.success(status == StatusConstant.ENABLE ? "用户启用成功" : "用户禁用成功");
    }

    /**
     * 分页查询用户（仅管理员）
     * @param userPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询用户")
    public Result<PageResult> pageQuery(UserPageQueryDTO userPageQueryDTO) {
        log.info("分页查询用户：{}", userPageQueryDTO);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

}
