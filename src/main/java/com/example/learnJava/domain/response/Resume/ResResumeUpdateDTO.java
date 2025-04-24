package com.example.learnJava.domain.response.Resume;

import com.example.learnJava.utils.constant.ResumeStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResResumeUpdateDTO {
    private Long id;
    private ResumeStatusEnum status;
}
