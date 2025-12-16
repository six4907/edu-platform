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
public class Enroll {
    private int id;
    private  int studentId;
    private int courseId;
    private boolean status;
    private LocalDateTime updateTime;
}
