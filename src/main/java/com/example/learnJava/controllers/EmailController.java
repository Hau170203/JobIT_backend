package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.service.EmailService;
import com.example.learnJava.service.SubscriberService;
import com.example.learnJava.utils.annotation.ApiMessage;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService){
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String getEmail() {
        // this.emailService.sendConfirmationEmail();
        // this.emailService.sendEmailSync("dinhvanduy1001@gmail.com","test Email" , "<h1><b> Hello world </b></h1>", false, true);
        // this.emailService.sendEmailFromTemplateSync("vanhau170203@gmail.com", "Write Email", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }

    
    
}
