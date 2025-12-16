package com.edu.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private Long id;
    private int userId;
    private String realName;
    private String title;
    private String introduction;
    private Long phone;
    private String email;
    private Integer teachingYears;
    private String avatar;
    private String nickName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
