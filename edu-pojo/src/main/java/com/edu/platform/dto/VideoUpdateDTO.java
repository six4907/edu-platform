package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "课时修改请求参数")
public class VideoUpdateDTO {

    @ApiModelProperty(value = "课时标题", required = true, example = "1.1 Java开发环境搭建(更新)")
    @NotBlank(message = "课时标题标题不能为空")
    private String title;

    @ApiModelProperty(value = "视频URL", example = "https://xxx.com/video-update.mp4")
    private String videoUrl; // 允许为空（支持后期上传）

    @ApiModelProperty(value = "是否免费（0-付费，1-免费）", required = true, example = "0")
    @NotNull(message = "是否免费标识不能为空")
    private Integer isFree;
}