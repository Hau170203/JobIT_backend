package com.example.learnJava.domain.response;

import com.example.learnJava.domain.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ResLoginDTO {
    @JsonProperty("access_token")
    private String access_token;
    private LoginUser user;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUser {
        private Long id;
        private String name;
        private String email;
        private Roles role;
    }

    @Setter
    @Getter
    public static class UserGetAccount {
        private LoginUser user;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private long Id;
        private String email;
        private String name;
    }

}
