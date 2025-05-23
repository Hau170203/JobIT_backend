package com.example.learnJava.service;

import com.example.learnJava.controllers.AuthController;
import com.example.learnJava.domain.Company;
import com.example.learnJava.domain.Roles;
import com.example.learnJava.domain.User;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.domain.response.User.ResCreateUserDTO;
import com.example.learnJava.domain.response.User.ResUserDTO;
import com.example.learnJava.repository.CompanyRepository;
import com.example.learnJava.repository.RoleReponsitory;
import com.example.learnJava.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final AuthController authController;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleReponsitory roleReponsitory;

    public UserService(UserRepository userRepository,
     AuthController authController,
     
      CompanyRepository companyRepository, RoleReponsitory roleReponsitory){
        this.userRepository = userRepository;
        this.authController = authController;
        this.companyRepository = companyRepository;
        this.roleReponsitory = roleReponsitory; 
    }


    public User handleCreateUser(User user) {
        if(user.getCompany() != null){
            Optional<Company> companyOp = this.companyRepository.findById(user.getCompany().getId());
            user.setCompany(companyOp.isPresent() ? companyOp.get() : null);
        } else {
            user.setCompany(null);
        }
        if(user.getRole() != null){
            Optional<Roles> roleOp = this.roleReponsitory.findById(user.getRole().getId());
            user.setRole(roleOp.isPresent() ? roleOp.get() : null);
        } else {
            user.setRole(null);
        }

        return  this.userRepository.save(user);
    }

    public void handleDeleteUser(long id){
        
        this.userRepository.deleteById(id);
    }

    public User handleGetUserById(long id){
        User user = this.userRepository.findById(id).orElse(null);
        return user;
    }

    public ResPageDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResPageDTO page  = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        meta.setPage(pageUser.getNumber() + 1);
        meta.setPageSize(pageUser.getSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());
        page.setMeta(meta);

        List<ResUserDTO> pageUserDTO = pageUser.getContent().stream().map(item -> new ResUserDTO(
            item.getId(),
            item.getUsername(),
            item.getEmail(),
            item.getAge(),
            item.getAddress(),
            item.getGender(),
            item.getCreatedAt(),
            item.getUpdatedAt(),
            new ResUserDTO.CompanyUser(
                item.getCompany() != null ? item.getCompany().getId() : 0,
                item.getCompany() != null ? item.getCompany().getName(): null
            ),
            new ResUserDTO.RoleUser(
                item.getRole() != null ? item.getRole().getId() : 0,
                item.getRole() != null ? item.getRole().getName() : null
            )

        )).collect(Collectors.toList());
        page.setData(pageUserDTO);
        return page;
    }

    public User handleUpdateUser(User user){
        User checkUser = this.userRepository.findById(user.getId()).orElse(null);

        if(user.getCompany() != null) {
            Optional<Company> company = this.companyRepository.findById(user.getCompany().getId());
            checkUser.setCompany(company.isPresent() ? company.get() : null);
        }

        if(user.getRole().getId() != null){
            Optional<Roles> roleOp = this.roleReponsitory.findById(user.getRole().getId());
            checkUser.setRole(roleOp.isPresent() ? roleOp.get() : null);
        }

        if(checkUser != null){
            checkUser.setUsername(user.getUsername());
            checkUser.setEmail(user.getEmail());

        }
        assert checkUser != null;
        return this.userRepository.save(checkUser);
    }

    public User handleByUser(String email){
        return this.userRepository.findByEmail(email);
    }

    public boolean handleExistsByEmail(String email){
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO handlCreateUserDTO(User user){
        ResCreateUserDTO newUser = new ResCreateUserDTO();
        ResCreateUserDTO.companyUser compaUser = new ResCreateUserDTO.companyUser();
        ResCreateUserDTO.RoleUser roleUser = new ResCreateUserDTO.RoleUser();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        // user.setPassword(user.getPassword());
        newUser.setAge(user.getAge());
        newUser.setAddress(user.getAddress());
        newUser.setGender(user.getGender());
        newUser.setCreatedAt(user.getCreatedAt());
        newUser.setId(user.getId());  
        
        if(user.getCompany() != null){
            compaUser.setId(user.getCompany().getId());
            compaUser.setName(user.getCompany().getName());
            newUser.setCompany(compaUser);
        }

        if(user.getRole() != null){
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            newUser.setRole(roleUser);
        }
        return newUser;
    }

    public ResCreateUserDTO handleUpdateUserDTO(User user){
        ResCreateUserDTO newUser = new ResCreateUserDTO();
        ResCreateUserDTO.companyUser company = new ResCreateUserDTO.companyUser();
        ResCreateUserDTO.RoleUser roleUser = new ResCreateUserDTO.RoleUser();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        // user.setPassword(user.getPassword());
        newUser.setAge(user.getAge());
        newUser.setAddress(user.getAddress());
        newUser.setGender(user.getGender());
        newUser.setCreatedAt(user.getCreatedAt());
        newUser.setId(user.getId());  
        
        if(user.getCompany() != null){
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
            newUser.setCompany(company);
        }

        if(user.getRole() != null){
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            newUser.setRole(roleUser);
        }
        return newUser;
    }

    public void handleRefreshToken(String refreshToken, String email){
        User user = this.userRepository.findByEmail(email);
        if(user != null){
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }

    }
    
}
