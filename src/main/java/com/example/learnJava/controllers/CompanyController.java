package com.example.learnJava.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.learnJava.domain.Company;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.service.CompanyService;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.errorException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @PostMapping("/companies")
    public ResponseEntity<Company> CreateCompany(@Valid @RequestBody Company data) {
        Company newCompany = this.companyService.createCompany(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("get all company success")
    public ResponseEntity<ResPageDTO> GetCompany(
        @Filter Specification<Company> spec,
        Pageable pageable
        // @RequestParam("current") Optional<String> currentOptional,
        // @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) throws errorException  {

        return  ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompany(spec, pageable));
    }
    
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getMethodName(@PathVariable Long id) throws errorException {
        Company company = this.companyService.getDetailCompany(id);
        if(company == null){
            throw new errorException("Company not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) throws errorException {
        Company company = this.companyService.getDetailCompany(id);
        if(company == null){
            throw new errorException("Company not found");
        }
        this.companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted successfully");
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany( @RequestBody Company entity) {
        Company company = this.companyService.updateCompany(entity);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }
    
}
