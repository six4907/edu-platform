package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(description = "C端用户登录入参")
public class UserLoginDTO implements Serializable {

    @ApiModelProperty(value = "登录ID（唯一）", required = true, example = "1001")
    @NotNull(message = "用户ID不能为空，且必须为数字")
    private Long id;

    @ApiModelProperty(value = "登录密码", required = true, example = "123456aA!")
    @NotBlank(message = "密码不能为空") // 非空校验
    private String password;

}