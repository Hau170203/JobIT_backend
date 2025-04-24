package com.example.learnJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.learnJava.domain.Roles;

public interface RoleReponsitory extends JpaRepository<Roles, Long>, JpaSpecificationExecutor<Roles> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    
} 
