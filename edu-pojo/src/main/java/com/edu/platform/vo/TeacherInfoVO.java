package com.edu.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师信息返回VO（适配学生/教师/管理员统一用户体系）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "教师信息返回VO（适配学生/教师/管理员统一用户体系）")
public class TeacherInfoVO implements Serializable {

    @ApiModelProperty(value = "教师工号", example = "10001")
    private Long id;

    @ApiModelProperty(value = "用户真实姓名", example = "张三")
    private String realName;

    @ApiModelProperty(value = "用户昵称", example = "张三")
    private String nickName;

    @ApiModelProperty(value = "用户职称", example = "教授")
    private String title;

    @ApiModelProperty(value = "用户简介", example = "从事计算机科学与技术教学10年，研究方向为人工智能")
    private String introduction;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@edu-platform.com")
    private String email;

    @ApiModelProperty(value = "教龄（年）", example = "10")
    private Integer teachingYears;

    @ApiModelProperty(value = "账号创建时间", example = "2025-01-01 10:00:00")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "信息更新时间", example = "2025-12-01 15:30:00")
    private LocalDateTime updateTime;
}