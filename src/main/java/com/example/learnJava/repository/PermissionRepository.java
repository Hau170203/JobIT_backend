package com.example.learnJava.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.learnJava.domain.Permissions;
import com.example.learnJava.domain.Roles;



public interface PermissionRepository extends JpaRepository<Permissions, Long>, JpaSpecificationExecutor<Permissions> {
    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);
    boolean existsByApiPathAndMethodAndModuleAndIdNot(String apiPath, String method, String module, Long id);
    List<Permissions> findByIdIn(List<Long> id);
    List<Permissions> findByRoles(Roles roles);
}
