package com.example.learnJava.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.learnJava.domain.Subscriber;
import com.example.learnJava.service.SubscriberService;
import com.example.learnJava.utils.error.IdInvalidException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


@Controller
@RequestMapping("/api/v1")
public class SubscriberController {
    
    private SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService){
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> createSubscribers(@Valid @RequestBody Subscriber sub) throws IdInvalidException {
        boolean isExist = this.subscriberService.isExistByEmail(sub.getEmail());
        if(isExist == true){
            throw new IdInvalidException("Email đã tồn tại ");
        }
        return ResponseEntity.status(201).body(this.subscriberService.createSubscriber(sub));
    }
    
    @PutMapping("/subscribers/{id}")
    public ResponseEntity<Subscriber> updateSubscribers( Subscriber newSub) throws IdInvalidException{
        
        Subscriber sub = this.subscriberService.getSubscriber(newSub.getId());

        if(sub == null){
            throw new IdInvalidException("Không tồn tại Subscriber này");
        }
        return ResponseEntity.ok().body(this.subscriberService.updateSubscriber(sub, newSub));
    }
        
}
