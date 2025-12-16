package com.edu.platform.dto;

import com.edu.platform.constant.PasswordConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "密码修改请求参数")
public class PasswordUpdateDTO {

    @ApiModelProperty(value = "旧密码", required = true, example = "oldPass123")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true, example = "newPass456")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = PasswordConstant.PASSWORD_COMPLEXITY_REGEX, message = PasswordConstant.PASSWORD_COMPLEXITY_MSG)
    private String newPassword;
}