package com.example.learnJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.learnJava.domain.Company;

public interface CompanyRepository extends  JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

} 