package com.example.learnJava.domain;

import java.time.Instant;

import com.example.learnJava.utils.SecurityUtils;
import com.example.learnJava.utils.constant.ResumeStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String email;
    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @PrePersist
    public void handleBeforeCreateBy(){
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get(): "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdateBy(){
        this.updatedBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get(): "";
        this.updatedAt = Instant.now();
    }
}
