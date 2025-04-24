package com.example.learnJava.domain.response.Resume;

import java.time.Instant;

import com.example.learnJava.utils.constant.ResumeStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeDTO {
    private Long id;
    private String email;
    private String url;
    private ResumeStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private User user;
    private Job job;
    

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private Long id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job {
        private Long id;
    }
    

}
