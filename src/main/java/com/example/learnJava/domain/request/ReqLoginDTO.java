package com.example.learnJava.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {
    @NotBlank(message = "Vui lòng nhập Email")
    private String userName;
    
    @NotBlank(message = "Vui lòng nhập password")
    private String password;

    

}
