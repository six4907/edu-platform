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
@ApiModel(description = "用户修改返回的数据格式") // 描述改为“用户”（适配学生/教师/管理员）
public class TeacherVO implements Serializable {
    @ApiModelProperty(value = "用户真实姓名", example = "张三")
    private String realName;

    @ApiModelProperty(value = "用户职称", example = "教授")
    private String title;

    @ApiModelProperty(value = "用户简介", example = "xxxxxxxxx")
    private String introduction;

    @ApiModelProperty(value = "教龄", example = "10")
    private Integer teachingYears;

    @ApiModelProperty(value = "用户昵称", example = "张三")
    private String nickName;

    @ApiModelProperty(value = "用户头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    @ApiModelProperty(value = "用户手机号", example = "12345678901")
    private String phone;

    @ApiModelProperty(value = "用户邮箱", example = "example@example.com")
    private String email;
}
