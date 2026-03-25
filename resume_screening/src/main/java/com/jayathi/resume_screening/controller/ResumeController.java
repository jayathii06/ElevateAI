package com.jayathi.resume_screening.controller;

import com.jayathi.resume_screening.model.Resume;
import com.jayathi.resume_screening.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public Resume uploadResume(@RequestParam("file") MultipartFile file) throws Exception {
        return resumeService.uploadResume(file);
    }

    @GetMapping
    public List<Resume> getAllResumes() {
        return resumeService.getAllResumes();
    }

    @GetMapping("/match/{resumeId}/{jobId}")
    public Map<String, Object> calculateMatch(
            @PathVariable Long resumeId,
            @PathVariable Long jobId) {
        return resumeService.calculateMatch(resumeId, jobId);
    }
}
