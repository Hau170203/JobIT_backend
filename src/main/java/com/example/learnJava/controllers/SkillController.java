package com.example.learnJava.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.learnJava.domain.skill;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.service.SkillService;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;





@Controller
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    private SkillController(SkillService skillService){
        this.skillService = skillService;
    }

    @PostMapping("skills")
    @ApiMessage("Tạo kỹ năng ")
    public ResponseEntity<skill> createSkill(@Valid @RequestBody com.example.learnJava.domain.skill skillOp) throws IdInvalidException {
        skill checkSkill = this.skillService.handleSkill(skillOp.getName());
        if(checkSkill != null){
            throw new IdInvalidException("Skill "+ skillOp.getName()+" đã tồn tại");
        }

        skill newSkill = this.skillService.createSkill(skillOp);

        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }


    @GetMapping("skills")
    @ApiMessage("get all skill")
    public ResponseEntity<ResPageDTO> getAllSkills(
        @Filter Specification<skill> spec,
        Pageable pageable
        // @RequestParam("current") Optional<String> currentOptional,
        // @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.getAllSkills(spec, pageable)) ;
    }

    @GetMapping("skills/{id}")
    @ApiMessage("Chi tiết kỹ năng")
    public ResponseEntity<skill> getDetailSkill(@PathVariable Long id) throws IdInvalidException {
        skill detailSkill = this.skillService.getDetailSkill(id);
        if (detailSkill == null) {
            throw new IdInvalidException("Không tìm thấy skill này");
        }
        return ResponseEntity.ok().body(detailSkill) ;
    }

    @PutMapping("skills")
    public ResponseEntity<skill> putMethodName( @RequestBody skill skillOp) throws IdInvalidException {
        skill updateSkill = this.skillService.updateSkill(skillOp);
        if(updateSkill == null) {
            throw new IdInvalidException("Không tìm thấy skill này");
        }
        return ResponseEntity.ok().body(updateSkill);
    }
    
}
