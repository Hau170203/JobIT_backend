package com.example.learnJava.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.learnJava.domain.skill;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public skill handleSkill(String name) {
        return this.skillRepository.findByName(name);
    }

    public skill createSkill(skill skillOp){
        return this.skillRepository.save(skillOp);
    }

    public ResPageDTO getAllSkills(Specification<skill> spec, Pageable pageable){
        Page<skill> skills = this.skillRepository.findAll(spec,pageable);
        ResPageDTO page = new ResPageDTO();
        ResPageDTO.meta  meta = new ResPageDTO.meta();
        meta.setPage(skills.getNumber() + 1);
        meta.setPageSize(skills.getSize());
        meta.setPages(skills.getTotalPages());
        meta.setTotal(skills.getTotalElements());
        page.setMeta(meta);
        page.setData(skills.getContent());

        return page;
    }

    public skill getDetailSkill(Long id) {
        return this.skillRepository.findById(id).orElse(null);
    }

    public skill updateSkill(skill skillOp){
        skill newSkill = this.skillRepository.findById(skillOp.getId()).orElse(null);
        if(newSkill == null){
            return null;
        }
        newSkill.setName(skillOp.getName());
        return this.skillRepository.save(newSkill);
    }
}
