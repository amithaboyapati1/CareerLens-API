package com.careerlens.api.controller;

import com.careerlens.api.entity.Resume;
import com.careerlens.api.service.ResumeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // Upload Resume
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) {

        try {

            Resume resume =
                    resumeService.uploadResume(file, email);

            return ResponseEntity.ok(resume);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Analyze Resume using AI Agent
    @GetMapping("/{id}/analyze")
    public ResponseEntity<?> analyzeResume(
            @PathVariable Long id) {

        try {

            Object result =
                    resumeService.analyzeResume(id);

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}