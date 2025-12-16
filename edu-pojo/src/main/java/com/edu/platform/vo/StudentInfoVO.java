package com.edu.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生信息返回VO（适配学生/教师/管理员统一用户体系）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoVO implements Serializable {
    @ApiModelProperty(value = "学号", example = "10001")
    private Long id;

    @ApiModelProperty(value = "用户真实姓名", example = "张三")
    private String realName;

    @ApiModelProperty(value = "学校", example = "清华大学")
    private String school;

    @ApiModelProperty(value = "年级", example = "大二")
    private String grade;

    @ApiModelProperty(value = "昵称", example = "张三")
    private String nikeName;

    @ApiModelProperty(value = "头像", example = "https://edu-platform.oss-cn-beijing.aliyuncs.com/avatar/default.png")
    private String avatar;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@edu-platform.com")
    private String email;

    @ApiModelProperty(value = "个性化简介", example = "张三，大二在清华大学")
    private String introduction;

    @ApiModelProperty(value = "账号创建时间", example = "2025-01-01 10:00:00")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "信息更新时间", example = "2025-12-01 15:30:00")
    private LocalDateTime updateTime;
}
