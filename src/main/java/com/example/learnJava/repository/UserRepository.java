package com.example.learnJava.repository;

import com.example.learnJava.domain.Company;
import com.example.learnJava.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;




public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    // User findByUsername(String username);
    List<User> findByCompany(Company company);
}
