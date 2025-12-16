package com.edu.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户登录返回的数据格式") // 描述改为“用户”（适配学生/教师/管理员）
public class UserLoginVO implements Serializable {

    @ApiModelProperty(value = "用户唯一ID", example = "1001")
    private Long id;

    @ApiModelProperty(value = "登录用户名（唯一）", example = "student001")
    private String username; // 字段名统一为 username（和实体类保持一致，避免混淆）

    @ApiModelProperty(value = "用户昵称（显示用）", example = "小明同学")
    private String nickname;

    @ApiModelProperty(value = "角色类型（1-学生，2-教师，3-管理员）", example = "1")
    private Integer role; // 补充角色字段，前端可根据角色展示不同菜单

    @ApiModelProperty(value = "头像URL（可为空）", example = "https://xxx.com/avatar.jpg")
    private String avatar; // 补充头像字段，前端直接展示

    @ApiModelProperty(value = "登录令牌（后续接口请求需携带）", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token; // 保留令牌字段，后续扩展 JWT 认证时直接使用
}