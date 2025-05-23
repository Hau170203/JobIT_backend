package com.example.learnJava.config;


import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learnJava.domain.Permissions;
import com.example.learnJava.domain.Roles;
import com.example.learnJava.domain.User;
import com.example.learnJava.repository.PermissionRepository;
import com.example.learnJava.repository.RoleReponsitory;
import com.example.learnJava.repository.UserRepository;
import com.example.learnJava.utils.constant.GenderEnum;


@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleReponsitory roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
            PermissionRepository permissionRepository,
            RoleReponsitory roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permissions> arr = new ArrayList<>();
            arr.add(new Permissions("Create a company", "/api/v1/companies", "POST", "COMPANIES"));
            arr.add(new Permissions("Update a company", "/api/v1/companies", "PUT", "COMPANIES"));
            arr.add(new Permissions("Delete a company", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
            arr.add(new Permissions("Get a company by id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
            arr.add(new Permissions("Get companies with pagination", "/api/v1/companies", "GET", "COMPANIES"));

            arr.add(new Permissions("Create a job", "/api/v1/jobs", "POST", "JOBS"));
            arr.add(new Permissions("Update a job", "/api/v1/jobs", "PUT", "JOBS"));
            arr.add(new Permissions("Delete a job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
            arr.add(new Permissions("Get a job by id", "/api/v1/jobs/{id}", "GET", "JOBS"));
            arr.add(new Permissions("Get jobs with pagination", "/api/v1/jobs", "GET", "JOBS"));

            arr.add(new Permissions("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permissions("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permissions("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permissions("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permissions("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            arr.add(new Permissions("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
            arr.add(new Permissions("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
            arr.add(new Permissions("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            arr.add(new Permissions("Get a resume by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            arr.add(new Permissions("Get resumes with pagination", "/api/v1/resumes", "GET", "RESUMES"));

            arr.add(new Permissions("Create a role", "/api/v1/roles", "POST", "ROLES"));
            arr.add(new Permissions("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            arr.add(new Permissions("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permissions("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            arr.add(new Permissions("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));

            arr.add(new Permissions("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permissions("Update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permissions("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permissions("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permissions("Get users with pagination", "/api/v1/users", "GET", "USERS"));

            arr.add(new Permissions("Create a subscriber", "/api/v1/subscribers", "POST", "SUBSCRIBERS"));
            arr.add(new Permissions("Update a subscriber", "/api/v1/subscribers", "PUT", "SUBSCRIBERS"));
            arr.add(new Permissions("Delete a subscriber", "/api/v1/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
            arr.add(new Permissions("Get a subscriber by id", "/api/v1/subscribers/{id}", "GET", "SUBSCRIBERS"));
            arr.add(new Permissions("Get subscribers with pagination", "/api/v1/subscribers", "GET", "SUBSCRIBERS"));

            arr.add(new Permissions("Download a file", "/api/v1/files", "POST", "FILES"));
            arr.add(new Permissions("Upload a file", "/api/v1/files", "GET", "FILES"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permissions> allPermissions = this.permissionRepository.findAll();

            Roles adminRole = new Roles();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("hn");
            adminUser.setAge(25);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setUsername("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

            Roles adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }

}

