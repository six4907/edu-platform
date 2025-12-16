package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "学生信息选填更新入参")
public class StudentUpdateDTO {

    @ApiModelProperty(value = "学校信息（选填）", example = "牛逼学校")
    private String school; // 选填

    @ApiModelProperty(value = "年级（选填）", example = "三年级")
    private String grade; // 选填

    @ApiModelProperty(value = "个性化简介（选填）", example = "王者百星，不服来战")
    private String introduction;

    @ApiModelProperty(value = "昵称（选填）", example = "小王")
    private String nickName;

    @ApiModelProperty(value = "头像（选填）", example = "https://edu-platform.oss-cn-beijing.aliyuncs.com/avatar/2021/05/05/20210505150001.png")
    private String avatar;
}
