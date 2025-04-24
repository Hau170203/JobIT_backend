package com.example.learnJava.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.learnJava.domain.Permissions;
import com.example.learnJava.domain.Roles;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.repository.PermissionRepository;
import com.example.learnJava.repository.RoleReponsitory;
import com.example.learnJava.utils.error.IdInvalidException;

@Service
public class RoleService {
    private final RoleReponsitory roleReponsitory;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleReponsitory roleReponsitory, PermissionRepository permissionRepository){
        this.roleReponsitory = roleReponsitory;
        this.permissionRepository = permissionRepository;
    }

    public Roles createRole(Roles role) throws IdInvalidException{
        boolean ischeck = this.roleReponsitory.existsByName(role.getName());

        if(ischeck){
            throw new IdInvalidException("Role đã tồn tại");
        }
        if(role.getPermissions() != null){
            List<Long> listPerIds = role.getPermissions()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());
            
            List<Permissions> listPer = this.permissionRepository.findByIdIn(listPerIds);
            role.setPermissions(listPer);
        }

        return this.roleReponsitory.save(role);
    }

    public Roles updateRole(Roles role) throws IdInvalidException {
        Roles checkRole = this.roleReponsitory.findById(role.getId()).orElse(null);
        if(checkRole == null) {
            throw new IdInvalidException("Không có role này!");
        }
        Boolean checkRoleId = this.roleReponsitory.existsByNameAndIdNot(role.getName(), role.getId());
        if(checkRoleId) {
            throw new IdInvalidException("Role này đã tồn tại");
        }

        if(role.getActive() != null){
            checkRole.setActive(role.getActive());
        }

        if(role.getName() != null){
            checkRole.setName(role.getName());
        }

        if(role.getDescription() != null){
            checkRole.setDescription(role.getDescription());
        }

        if(role.getPermissions() != null){
            List<Long> listPerIds = role.getPermissions()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());
            
            List<Permissions> listPer = this.permissionRepository.findByIdIn(listPerIds);
            checkRole.setPermissions(listPer);
        }

        return this.roleReponsitory.save(checkRole);
    }

    public ResPageDTO getAllRole(Specification<Roles> spec, Pageable pageable){
        Page<Roles> listRoles = this.roleReponsitory.findAll(spec, pageable); 
        ResPageDTO page = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        meta.setPage(listRoles.getNumber()+ 1);
        meta.setPageSize(listRoles.getSize());
        meta.setPages(listRoles.getTotalPages());
        meta.setTotal(listRoles.getTotalElements());
        page.setMeta(meta);
        page.setData(listRoles.getContent());
        return page;
    }

    public String deleteRole(Long id) {
        Optional<Roles> roleOptional = this.roleReponsitory.findById(id);
    
        if (roleOptional.isEmpty()) {
            return "Không tồn tại role này";
        }
        // Xoá role
        this.roleReponsitory.deleteById(id);
        return "Xoá role thành công!";
    }
}
