package com.example.learnJava.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.learnJava.domain.Company;
import com.example.learnJava.domain.Job;
import com.example.learnJava.domain.skill;
import com.example.learnJava.domain.response.ResJobDTO;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.repository.CompanyRepository;
import com.example.learnJava.repository.JobRepository;
import com.example.learnJava.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    // Xử lý DTO

    public ResJobDTO JobDTO(Job job) {

        ResJobDTO res = new ResJobDTO();

        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());

        // Xử lý phần company

        ResJobDTO.companyUser com = new ResJobDTO.companyUser();
        com.setId(job.getCompany().getId());
        com.setLogo(job.getCompany().getLogo());
        com.setName(job.getCompany().getName());
        res.setCompany(com);
        res.setQuantity(job.getQuantity());
        res.setSalary(job.getSalary());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.getActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setCreatedBy(job.getCreatedBy());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setUpdateBy(job.getUpdateBy());

        List<String> Skills = job.getSkills().stream().map(x -> x.getName()).collect(Collectors.toList());
        res.setSkills(Skills);

        return res;
    }

    // Xử lý trả về ControllerController
    public Job handleCheckName(String name) {
        return this.jobRepository.findByName(name);
    }

    public ResJobDTO createJob(Job job) {
        if (job.getSkills() != null) {
            List<Long> listSkillIds = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<skill> listSkills = this.skillRepository.findByIdIn(listSkillIds);
            job.setSkills(listSkills);
        }

        if (job.getCompany() != null) {
            Company com = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            if (com != null) {
                job.setCompany(com);
            }
        }
        Job newJob = this.jobRepository.save(job);

        ResJobDTO JobDTO = this.JobDTO(newJob);
        return JobDTO;
    }

    public ResJobDTO getDetailJob(Long id) {
        Job checkJob = this.jobRepository.findById(id).orElse(null);
        ResJobDTO JobDTO = this.JobDTO(checkJob);
        return JobDTO;
    }

    public ResPageDTO getAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> listJobs = this.jobRepository.findAll(spec, pageable);
        if (listJobs == null) {
            return null;
        }
        ResPageDTO page = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        List<ResJobDTO> listJobDTOs = listJobs.stream().map(x -> new ResJobDTO(
                x.getId(),
                x.getName(),
                x.getLocation(),
                x.getQuantity(),
                x.getSalary(),
                x.getLevel(),
                x.getDescription(),
                x.getStartDate(),
                x.getEndDate(),
                x.getActive(),
                x.getCreatedAt(),
                x.getUpdatedAt(),
                x.getCreatedBy(),
                x.getUpdateBy(),
                x.getCompany() != null ? new ResJobDTO.companyUser(
                        x.getCompany().getId(),
                        x.getCompany().getName(),
                        x.getCompany().getLogo()) : null,
                x.getSkills().stream().map(skill::getName).collect(Collectors.toList()))).collect(Collectors.toList());
        meta.setPage(listJobs.getNumber() + 1);
        meta.setPageSize(listJobs.getSize());
        meta.setPages(listJobs.getTotalPages());
        meta.setTotal(listJobs.getTotalElements());
        page.setMeta(meta);
        page.setData(listJobDTOs);

        return page;
    }

    public ResJobDTO updateJob(Job job) {
        Job checkJob = this.jobRepository.findById(job.getId()).orElse(null);

        if (checkJob == null) {
            return null;
        }

        checkJob.setName(job.getName());
        checkJob.setLocation(job.getLocation());
        checkJob.setQuantity(job.getQuantity());
        checkJob.setSalary(job.getSalary());
        checkJob.setLevel(job.getLevel());
        checkJob.setDescription(job.getDescription());
        checkJob.setStartDate(job.getStartDate());
        checkJob.setEndDate(job.getEndDate());
        checkJob.setActive(job.getActive());
        checkJob.setCreatedAt(job.getCreatedAt());
        checkJob.setCreatedBy(job.getCreatedBy());
        checkJob.setUpdatedAt(job.getUpdatedAt());
        checkJob.setUpdateBy(job.getUpdateBy());
        checkJob.setCompany(job.getCompany());
        checkJob.setSkills(job.getSkills());
        if (job.getSkills() != null) {
            List<Long> listSkillIds = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<skill> listSkills = this.skillRepository.findByIdIn(listSkillIds);
            checkJob.setSkills(listSkills);
        }
        if (job.getCompany() != null) {
            Company com = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            if (com != null) {
                checkJob.setCompany(com);
            }
        }
        // checkJob.setCompany(job.getCompany());

        Job newJob = this.jobRepository.save(checkJob);
        ResJobDTO JobDTO = this.JobDTO(newJob);
        return JobDTO;
    }

    public void deleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }
}
