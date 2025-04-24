package com.example.learnJava.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.utils.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    // @CrossOrigin
    public String hello() throws IdInvalidException {
        // if(true)
        //     throw new IdInvalidException("check Hello");
        return  "update Hello World";
    }
}
