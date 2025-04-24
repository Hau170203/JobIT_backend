package com.example.learnJava.domain.response.User;

import java.time.Instant;

import com.example.learnJava.utils.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateUserDTO {
    private Long id;
    private String username;
    private String email;
    private int age;
    private String address;
    private GenderEnum gender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;
    public companyUser company;
    public RoleUser role;

    @Setter
    @Getter
    public static class companyUser {
        private Long id;
        private String name;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private Long id;
        private String name;
    }
}
