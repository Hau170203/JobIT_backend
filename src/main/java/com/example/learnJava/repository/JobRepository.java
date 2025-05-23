package com.example.learnJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.learnJava.domain.Job;
import java.util.List;
import com.example.learnJava.domain.skill;


public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Job findByName(String name);

    List<Job> findBySkillsIn(List<skill> skills);
    
}
