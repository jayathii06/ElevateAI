package com.jayathi.resume_screening.service;

import com.jayathi.resume_screening.model.Job;
import com.jayathi.resume_screening.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job createJob(Map<String, String> body) {
        Job job = Job.builder()
                .title(body.get("title"))
                .requiredSkills(body.get("requiredSkills"))
                .build();
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
