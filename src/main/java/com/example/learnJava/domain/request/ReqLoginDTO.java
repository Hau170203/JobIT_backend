package com.example.learnJava.domain.request;

import jakarta.validation.constraints.NotBlank;

public class ReqLoginDTO {
    @NotBlank(message = "Vui lòng nhập Email")
    private String username;
    
    @NotBlank(message = "Vui lòng nhập password")
    private String password;

    public void setUserName(String username){
        this.username = username;
    }

    public String getUserName(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassWord(){
        return this.password;
    }

}
