package com.jayathi.resume_screening.service;

import com.jayathi.resume_screening.model.Resume;
import com.jayathi.resume_screening.model.Job;
import com.jayathi.resume_screening.repository.ResumeRepository;
import com.jayathi.resume_screening.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobRepository jobRepository;

    public Resume uploadResume(MultipartFile file) throws Exception {
        String content = new String(file.getBytes()).toLowerCase();
        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .content(content)
                .build();
        return resumeRepository.save(resume);
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    public Map<String, Object> calculateMatch(Long resumeId, Long jobId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        Optional<Job> jobOpt = jobRepository.findById(jobId);

        if (resumeOpt.isEmpty()) {
            response.put("error", "Resume not found with ID: " + resumeId);
            return response;
        }
        if (jobOpt.isEmpty()) {
            response.put("error", "Job not found with ID: " + jobId);
            return response;
        }

        String resumeText = resumeOpt.get().getContent().toLowerCase();
        String[] skills = jobOpt.get().getRequiredSkills().toLowerCase().split("[,\\s]+");

        int matched = 0;
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String skill : skills) {
            skill = skill.trim();
            if (!skill.isEmpty()) {
                if (resumeText.contains(skill)) {
                    matched++;
                    matchedSkills.add(skill);
                } else {
                    missingSkills.add(skill);
                }
            }
        }

        int percentage = skills.length > 0
                ? (int) Math.round((matched * 100.0) / skills.length)
                : 0;

        response.put("matchPercentage", percentage);
        response.put("matchedSkills", matchedSkills);
        response.put("missingSkills", missingSkills);
        response.put("totalSkills", skills.length);
        response.put("matchedCount", matched);
        return response;
    }
}
