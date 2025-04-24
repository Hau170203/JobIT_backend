package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.Roles;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.service.RoleService;
import com.example.learnJava.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1")
public class RolesController {
    private final RoleService roleService;

    public RolesController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("roles")
    public ResponseEntity<Roles> createRole(@RequestBody Roles role) throws IdInvalidException {
        
        return ResponseEntity.status(201).body(this.roleService.createRole(role)) ;
    }

    @PutMapping("/roles")
    public ResponseEntity<Roles> updateRole(@RequestBody Roles role) throws IdInvalidException {

        return ResponseEntity.status(200).body(this.roleService.updateRole(role));
    }

    @GetMapping("/roles")
    public ResponseEntity<ResPageDTO> getAllRole(
        @Filter Specification<Roles> spec,
        Pageable pageable
    ) {
        return ResponseEntity.ok().body(this.roleService.getAllRole(spec, pageable));
    }
    
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id){
        return ResponseEntity.ok().body(this.roleService.deleteRole(id));
    }
    
    
}

