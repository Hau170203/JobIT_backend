package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.Permissions;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.service.PermissionService;
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
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<Permissions> createPermission(@RequestBody Permissions per) throws IdInvalidException {
        
        return ResponseEntity.status(201).body(this.permissionService.createPermission(per));
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permissions> putMethodName(@RequestBody Permissions per) throws IdInvalidException {
        
        return ResponseEntity.status(200).body(this.permissionService.updatePermission(per));
    }

    @GetMapping("/permissions")
    public ResponseEntity<ResPageDTO> getAllPermissions(
        @Filter Specification<Permissions> spec,
        Pageable pageable
    ) {
        return ResponseEntity.ok().body(this.permissionService.getAllPermission(spec, pageable)) ;
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<String> deletePermissions(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.permissionService.deletePermission(id));
    }
}
