package com.example.learnJava.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.learnJava.domain.Resume;
import com.example.learnJava.domain.User;
import com.example.learnJava.domain.response.ResJobDTO;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.domain.response.Resume.ResResumeByUserDTO;
import com.example.learnJava.domain.response.Resume.ResResumeDTO;
import com.example.learnJava.domain.response.Resume.ResResumeUpdateDTO;
import com.example.learnJava.repository.ResumeRepository;
import com.example.learnJava.repository.UserRepository;
import com.example.learnJava.utils.SecurityUtils;
import com.example.learnJava.utils.constant.ResumeStatusEnum;
import com.example.learnJava.utils.error.IdInvalidException;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobService jobService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    FilterBuilder fb;

    @Autowired
    FilterParser filterParser;

    @Autowired
    FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository, JobService jobService, UserService userService, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.jobService = jobService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Resume DTO
    public ResResumeDTO convertToDTO(Resume resume) {
        ResResumeDTO dto = new ResResumeDTO();
        dto.setId(resume.getId());
        dto.setEmail(resume.getEmail());
        dto.setUrl(resume.getUrl());
        dto.setStatus(resume.getStatus());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setUpdatedAt(resume.getUpdatedAt());
        dto.setCreatedBy(resume.getCreatedBy());
        dto.setUpdatedBy(resume.getUpdatedBy());

        ResResumeDTO.User userDTO = new ResResumeDTO.User();
        userDTO.setId(resume.getUser().getId());
        dto.setUser(userDTO);

        ResResumeDTO.Job jobDTO = new ResResumeDTO.Job();
        jobDTO.setId(resume.getJob().getId());
        dto.setJob(jobDTO);
        return dto;
    }

    public ResResumeByUserDTO convertToDTO2(Resume resume) {
        ResResumeByUserDTO dto = new ResResumeByUserDTO();
        dto.setId(resume.getId());
        dto.setEmail(resume.getEmail());
        dto.setUrl(resume.getUrl());
        dto.setStatus(resume.getStatus());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setUpdatedAt(resume.getUpdatedAt());
        dto.setCreatedBy(resume.getCreatedBy());
        dto.setUpdatedBy(resume.getUpdatedBy());

        ResResumeByUserDTO.User userDTO = new ResResumeByUserDTO.User();
        userDTO.setId(resume.getUser().getId());
        userDTO.setName(resume.getUser().getUsername());
        dto.setUser(userDTO);

        ResResumeByUserDTO.Job jobDTO = new ResResumeByUserDTO.Job();
        jobDTO.setId(resume.getJob().getId());
        jobDTO.setName(resume.getJob().getName());
        dto.setJob(jobDTO);

        ResResumeByUserDTO.CompanyUser com = new ResResumeByUserDTO.CompanyUser();
        com.setId(resume.getJob().getCompany().getId());
        com.setName(resume.getJob().getCompany().getName());
        dto.setCompany(com);

        return dto;
    }

    

    public ResResumeDTO createResume(Resume resume) throws IdInvalidException{
        ResJobDTO job = jobService.getDetailJob(resume.getJob().getId());
        if (job == null) {
            throw new IdInvalidException("Job not found");
        }

        User user = userService.handleGetUserById(resume.getUser().getId());
        if (user == null) {
            throw new IdInvalidException("User not found");
        }

        Resume  newResume = resumeRepository.save(resume);

        if(newResume != null) {
            ResResumeDTO resResumeDTO = convertToDTO(newResume);
            return resResumeDTO;
        } else {
            throw new IdInvalidException("Failed to create resume");
        }
    }

    public ResResumeDTO updateResume(ResResumeUpdateDTO resume) throws IdInvalidException {
        if(resume.getId() == null){
            throw new IdInvalidException("Vui long nhap id ");
        }
        Resume resume2 = resumeRepository.findById(resume.getId()).orElse(null);
        if(resume2 == null) {
            throw new IdInvalidException("k co resume nay trong he thong");
        }
        
        if(ResumeStatusEnum.valueOf(resume.getStatus().name()) == null) {
            throw new IdInvalidException("status khong hop le");
        }
        resume2.setStatus(resume.getStatus());
        Resume updatedResume = resumeRepository.save(resume2);
        return convertToDTO(updatedResume);
    }

    public void deleteResume(Long id) throws IdInvalidException {
        Resume resume = resumeRepository.findById(id).orElse(null);
        if(resume == null) {
            throw new IdInvalidException("k co resume nay trong he thong");
        }
        resumeRepository.delete(resume);
    }

    public ResPageDTO getAllResumes( Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumes = this.resumeRepository.findAll(spec, pageable);
        ResPageDTO page = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        meta.setPage(resumes.getNumber() +1);
        meta.setPageSize(resumes.getSize());
        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());
        page.setMeta(meta);
        page.setData(resumes.getContent().stream().map(this::convertToDTO2).collect(Collectors.toList())
);
        return page;
    }

    public ResPageDTO fetchResumeByUser(Pageable pageable){
        String email = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email = '" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResPageDTO  page = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        meta.setPage(pageResume.getNumber() + 1);
        meta.setPages(pageResume.getTotalPages());
        meta.setPageSize(pageResume.getSize());
        meta.setTotal(pageResume.getTotalElements());
        page.setMeta(meta);
        page.setData(pageResume.getContent().stream().map(this::convertToDTO2).collect(Collectors.toList()));
        // page.setData(pageResume.getContent());
        return page;
    }

    public Resume fetchResumeById(Long id){
        Resume resume = this.resumeRepository.findById(id).orElse(null);
        return resume;
    }
}
