package com.example.learnJava.service;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.learnJava.domain.Permissions;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.repository.PermissionRepository;
import com.example.learnJava.utils.error.IdInvalidException;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public Permissions createPermission(Permissions permission) throws IdInvalidException{
        boolean isCheck = this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
        if(isCheck == true){
            throw new IdInvalidException("Permission đã tồn tại");
        }
        Permissions newPermissions = this.permissionRepository.save(permission);
        return newPermissions;
    }

    public Permissions updatePermission(Permissions permission) throws IdInvalidException {
        Permissions checkper = this.permissionRepository.findById(permission.getId()).orElse(null);
        if(checkper == null) {
            throw new IdInvalidException("Không tìm thây permission này");
        }
        boolean isCheck = this.permissionRepository.existsByApiPathAndMethodAndModuleAndIdNot(permission.getApiPath(),permission.getMethod(), permission.getModule(), permission.getId());
        if (isCheck == true) {
            throw  new IdInvalidException("permission đã tồn tại");
        }
        checkper.setName(permission.getName());
        checkper.setApiPath(permission.getApiPath());
        checkper.setMethod(permission.getMethod());
        checkper.setModule(permission.getModule());
        return this.permissionRepository.save(checkper);
    }

    public ResPageDTO getAllPermission(Specification<Permissions> spec, Pageable pageable){
        Page<Permissions> permissions = this.permissionRepository.findAll(spec, pageable);
        ResPageDTO page = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        meta.setPage(permissions.getNumber() + 1);
        meta.setPages(permissions.getTotalPages());
        meta.setPageSize(permissions.getSize());
        meta.setTotal(permissions.getTotalElements());
        page.setMeta(meta);
        page.setData(permissions.getContent());
        return page;
    }

    public String deletePermission(Long id){
        Optional<Permissions> permissionOp = this.permissionRepository.findById(id);
        Permissions currentPermission = permissionOp.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        // List<?> list = currentPermission.getRoles();
        this.permissionRepository.delete(currentPermission);
        return "Xóa thành công";
    }
}
