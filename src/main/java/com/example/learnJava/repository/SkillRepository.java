package com.example.learnJava.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.learnJava.domain.skill;


public interface SkillRepository extends JpaRepository<skill, Long>, JpaSpecificationExecutor<skill> {
    skill findByName(String name);
    List<skill> findByIdIn(List<Long> ids);
} 
