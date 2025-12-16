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
public class Student {
    private Long id;
    private int userId;
    private String realName;
    private String school;
    private String grade;
    private String nickName;
    private String avatar;
    private String phone;
    private String email;
    private String introduction;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
