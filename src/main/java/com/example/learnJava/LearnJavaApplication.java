package com.example.learnJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LearnJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnJavaApplication.class, args);
	}
}
