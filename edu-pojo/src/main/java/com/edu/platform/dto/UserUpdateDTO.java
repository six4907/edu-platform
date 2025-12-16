package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@ApiModel(description = "用户信息修改请求参数")
public class UserUpdateDTO {

    @ApiModelProperty(value = "用户昵称（可选，长度2-20位）", example = "新昵称")
    @Length(min = 2, max = 20, message = "昵称长度必须在2-20位之间")
    private String nickname; // 可选修改

    @ApiModelProperty(value = "头像URL（可选）", example = "https://xxx.com/new-avatar.jpg")
    private String avatar; // 可选修改

    @ApiModelProperty(value = "电子邮箱（可选，格式验证）", example = "user@example.com")
    @Email(message = "邮箱格式不正确")
    private String email; // 可选修改
}