package com.example.learnJava.domain;

import java.time.Instant;
import java.util.List;

import com.example.learnJava.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skill")
@Getter
@Setter

public class skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
    private String createdBy;
    private String updateBy;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;


    @PrePersist
    public void handleBeforeCreateBy(){
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get(): "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdateBy(){
        this.updateBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get(): "";
        this.updatedAt = Instant.now();
    }
}
