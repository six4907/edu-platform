package com.edu.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "课时添加请求参数")
public class VideoAddDTO {

    @ApiModelProperty(value = "章节ID", required = true, example = "1")
    @NotNull(message = "章节ID不能为空")
    private Long chapterId;

    @ApiModelProperty(value = "课时标题", required = true, example = "1.1 Java开发环境搭建")
    @NotBlank(message = "课时标题不能为空")
    private String title;

    @ApiModelProperty(value = "视频URL", example = "https://xxx.com/video.mp4")
    private String videoUrl; // 允许为空（支持后期上传）

    @ApiModelProperty(value = "视频时长（格式HH:mm:ss）", example = "00:15:30")
    private String duration;

    @ApiModelProperty(value = "是否免费（0-付费，1-免费）", required = true, example = "1")
    @NotNull(message = "是否免费标识不能为空")
    private Integer isFree;
}