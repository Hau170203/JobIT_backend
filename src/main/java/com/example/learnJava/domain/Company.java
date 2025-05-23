package com.example.learnJava.domain;

import java.time.Instant;
import java.util.List;

import com.example.learnJava.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message= "Tên công ty không thể để trống !")
    private String name;
    
    @Column(columnDefinition =  "MediumText")
    private String description;
    private String address;
    private String logo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
    private String createBy;
    private String updateBy;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> users;
    
    @OneToMany(mappedBy = "company", fetch =  FetchType.LAZY)
    @JsonIgnore
    List<Job> jobs;


    @PrePersist
    public void handleBeforeCreateBy(){
        this.createBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get(): "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdateBy(){
        this.updateBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get(): "";
        this.updatedAt = Instant.now();
    }
}
