package com.example.learnJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnJava.domain.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long>  {
    Boolean existsByEmail(String name);
}
