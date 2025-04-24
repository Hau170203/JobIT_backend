package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.Job;
import com.example.learnJava.domain.response.ResJobDTO;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.service.JobService;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;
    
    public  JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create Job")
    public ResponseEntity<ResJobDTO> CreateJob(@RequestBody Job job ) {
        ResJobDTO newJob = this.jobService.createJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(newJob) ;
    }
    
    @GetMapping("jobs/{id}")
    @ApiMessage("get Detail Job")
    public ResponseEntity<ResJobDTO> getDetailJob(@PathVariable Long id) throws IdInvalidException {
        ResJobDTO checkJob = this.jobService.getDetailJob(id);
        if(checkJob == null ){
            throw new IdInvalidException("Không tìm thấy job này");
        }

        return ResponseEntity.ok().body(checkJob);
    }

    @GetMapping("/jobs")
    @ApiMessage("get All Job")
    public ResponseEntity<ResPageDTO> getAllJob(
        @Filter Specification<Job> spec,
        Pageable pageable
    ) {
        return ResponseEntity.ok(this.jobService.getAllJob(spec, pageable));
    }

    @PutMapping("jobs")
    public ResponseEntity<ResJobDTO> updateJob( @RequestBody Job job) {

        ResJobDTO updatJob = this.jobService.updateJob(job);
        
        return ResponseEntity.ok().body(updatJob);
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete Job")
    
    public ResponseEntity<String> deleteJob(@PathVariable Long id) throws IdInvalidException {
        ResJobDTO checkJob = this.jobService.getDetailJob(id);
        if(checkJob == null ){
            throw new IdInvalidException("Không tìm thấy job này");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.ok().body("Xóa job thành công");
    }
    
}


