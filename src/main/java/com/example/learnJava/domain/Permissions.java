package com.example.learnJava.domain;

import java.time.Instant;
import java.util.List;

import com.example.learnJava.utils.SecurityUtils;
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
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name không được để trống")
    private String name;
    @NotBlank(message = "api không được để trống")
    private String apiPath;
    @NotBlank(message = "method không được để trống")
    private String method;
    @NotBlank(message = "Module không được để trống")
    private String module;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Roles> roles;

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