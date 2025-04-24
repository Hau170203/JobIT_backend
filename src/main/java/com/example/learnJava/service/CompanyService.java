package com.example.learnJava.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.learnJava.domain.Company;
import com.example.learnJava.domain.User;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.repository.CompanyRepository;
import com.example.learnJava.repository.UserRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company createCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResPageDTO getAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> companies = this.companyRepository.findAll(spec, pageable);
        ResPageDTO page = new ResPageDTO();
        ResPageDTO.meta meta = new ResPageDTO.meta();
        meta.setPage(companies.getNumber() +1);
        meta.setPageSize(companies.getSize());
        meta.setPages(companies.getTotalPages());
        meta.setTotal(companies.getTotalElements());
        page.setMeta(meta);
        page.setData(companies.getContent());
        return page;
    }

    public Company getDetailCompany(Long id) {
        Company company = this.companyRepository.findById(id).orElse(null);
        if (company == null) {
            return null;
        }

        return company;
    }

    public void deleteCompany(Long id) {
        Optional<Company> comOptional = this.companyRepository.findById(id);
        if(comOptional.isPresent()) {
            Company com = comOptional.get();
            List<User> listUser = this.userRepository.findByCompany(com); 
            this.userRepository.deleteAll(listUser);
        }

        this.companyRepository.deleteById(id);
    }

    public Company updateCompany(Company company) {
        Company checkCompany = this.companyRepository.findById(company.getId()).orElse(null);
        if (checkCompany != null) {
            if (company.getName() != null) {
                checkCompany.setName(company.getName());
            }
            if (company.getDescription() != null) {
                checkCompany.setDescription(company.getDescription());
            }
            if (company.getAddress() != null) {
                checkCompany.setAddress(company.getAddress());
            }
            if (company.getLogo() != null) {
                checkCompany.setLogo(company.getLogo());
            }
        } else {
            return null;
        }

        return this.companyRepository.save(checkCompany);

    }
}
