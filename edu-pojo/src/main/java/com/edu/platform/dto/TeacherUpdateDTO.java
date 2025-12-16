package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Data
@ApiModel(description = "教师信息修改请求参数")
public class TeacherUpdateDTO {

    @ApiModelProperty(value = "教师真实姓名（可选，2-20位）", example = "张教授")
    @Length(max = 20, message = "真实姓名长度必须在2-20位之间")
    private String realName;

    @ApiModelProperty(value = "教师头衔（可选，如教授/讲师）", example = "高级讲师")
    private String title;

    @ApiModelProperty(value = "教师简介（可选，不超过500字）", example = "10年Java教学经验，曾任某大厂技术总监")
    @Length(max = 500, message = "简介内容不能超过500字")
    private String introduction;

    @ApiModelProperty(value = "教龄", example = "10")
    @Min(value = 0, message = "教龄不能小于0")
    @Max(value = 100, message = "教龄不能超过100")
    private Integer teachingYears;

    @ApiModelProperty(value = "昵称", example = "小伟")
    private String nickName;

    @ApiModelProperty(value = "头像", example = "https://edu-platform.oss-cn-beijing.aliyuncs.com/avatar/2023/05/05/1.png")
    private String avatar;

}