package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.Company;
import com.example.learnJava.domain.Job;
import com.example.learnJava.domain.Resume;
import com.example.learnJava.domain.User;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.domain.response.Resume.ResResumeDTO;
import com.example.learnJava.domain.response.Resume.ResResumeUpdateDTO;
import com.example.learnJava.service.ResumeService;
import com.example.learnJava.service.UserService;
import com.example.learnJava.utils.SecurityUtils;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserService userService;
    private final FilterBuilder filterBuilder;

     @Autowired
    FilterBuilder fb;

    @Autowired
    FilterParser filterParser;

    @Autowired
    FilterSpecificationConverter filterSpecificationConverter;

    

    public ResumeController(ResumeService resumeService, UserService userService,  FilterBuilder filterBuilder) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
    }
    
    @PostMapping("/resumes")
    public ResponseEntity<ResResumeDTO> createResumes(@RequestBody Resume resume) throws IdInvalidException {
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
        List<Long> arrJobIds = null;
        String email = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.handleByUser(email);
        if(currentUser != null ){
            Company userCompany = currentUser.getCompany();
            if(userCompany != null){
                List<Job> comJobs = userCompany.getJobs();
                if(comJobs != null && comJobs.size() > 0){
                    arrJobIds = comJobs.stream().map(x -> x.getId()).collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);
        return ResponseEntity.status(200).body(this.resumeService.getAllResumes(finalSpec, pageable));
    }


    @PostMapping("/resumes/by-user")
    public ResponseEntity<ResPageDTO> fetchResumesByUser(Pageable page) {
        
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(page));
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<Resume> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.resumeService.fetchResumeById(id));
    }
    
}
