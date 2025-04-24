package com.example.learnJava.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ResLoginDTO {
    private String accessToken;
    private LoginUser user;
    private RoleUser1 role;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUser {
        private Long id;
        private String name;
        private String email;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser1 {
        private Long id;
        private String name;
    }

}
