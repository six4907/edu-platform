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
public class StudentVO implements Serializable {
    @ApiModelProperty(value = "学生ID", example = "1001")
    private Long id;

    @ApiModelProperty(value = "真实姓名", example = "张三")
    private String real_name;

    @ApiModelProperty(value = "学校信息", example = "XX中学")
    private String school;

    @ApiModelProperty(value = "年级", example = "高一")
    private String grade;


}
