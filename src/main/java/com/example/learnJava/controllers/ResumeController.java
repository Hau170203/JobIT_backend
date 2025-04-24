package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.Resume;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.domain.response.Resume.ResResumeDTO;
import com.example.learnJava.domain.response.Resume.ResResumeUpdateDTO;
import com.example.learnJava.service.ResumeService;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }
    
    @PostMapping("/resumes")
    public ResponseEntity<ResResumeDTO> createResumes(@RequestBody Resume resume) throws IdInvalidException {
        //TODO: process POST request

        return ResponseEntity.status(201).body(this.resumeService.createResume(resume));
    }

    @PutMapping("/resumes")
    public ResponseEntity<ResResumeDTO> updateResume(@RequestBody ResResumeUpdateDTO resume) throws IdInvalidException {

        return ResponseEntity.status(200).body(this.resumeService.updateResume(resume));
    }
    
    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resumes")
    public ResponseEntity<String> deleteResume(@PathVariable Long id ) throws IdInvalidException {
        this.resumeService.deleteResume(id);
        return ResponseEntity.status(204).body("Xóa thành công !");
    }


    @GetMapping("/resumes")
    public ResponseEntity<ResPageDTO> getAllResumes(
        @Filter Specification<Resume> spec,
        Pageable pageable
    ) {
        return ResponseEntity.status(200).body(this.resumeService.getAllResumes(spec, pageable));
    }


    @PostMapping("/resumes/By-user")
    public ResponseEntity<ResPageDTO> postMethodName(Pageable page) {
        
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(page));
    }
    
    
}
