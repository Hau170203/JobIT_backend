package com.example.learnJava.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.learnJava.domain.Job;
import com.example.learnJava.domain.Subscriber;
import com.example.learnJava.domain.skill;
import com.example.learnJava.domain.response.ResEmailJob;
import com.example.learnJava.repository.JobRepository;
import com.example.learnJava.repository.SkillRepository;
import com.example.learnJava.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private SubscriberRepository subscriberRepository;
    private SkillRepository skillRepository;
    private JobRepository jobRepository;
    private EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, JobRepository jobRepository,
            EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public Boolean isExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber createSubscriber(Subscriber sub) {
        if (sub.getSkills() != null) {
            List<Long> reqSkills = sub.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            sub.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(sub);
    }

    public Subscriber updateSubscriber(Subscriber sub, Subscriber newSub) {
        // check skills
        if (newSub.getSkills() != null) {
            List<Long> listId = newSub.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<skill> listSkill = this.skillRepository.findByIdIn(listId);
            sub.setSkills(listSkill);
        }

        return this.subscriberRepository.save(sub);
    }

    public Subscriber getSubscriber(Long id) {
        return this.subscriberRepository.findById(id).orElse(null);
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

}
