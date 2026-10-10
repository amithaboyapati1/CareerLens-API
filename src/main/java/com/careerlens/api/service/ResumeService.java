package com.careerlens.api.service;

import com.careerlens.api.entity.Resume;
import com.careerlens.api.repository.ResumeRepository;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AiService aiService;

    private final String uploadDirectory = "uploads/resumes/";

    public ResumeService(
            ResumeRepository resumeRepository,
            AiService aiService) {

        this.resumeRepository = resumeRepository;
        this.aiService = aiService;
    }

    public Resume uploadResume(
            MultipartFile file,
            String userEmail
    ) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please upload a resume file");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new RuntimeException("Invalid file name");
        }

        String lowerCaseName = fileName.toLowerCase();

        if (!lowerCaseName.endsWith(".pdf")
                && !lowerCaseName.endsWith(".docx")) {

            throw new RuntimeException(
                    "Only PDF and DOCX files are allowed"
            );
        }

        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String uniqueFileName =
                System.currentTimeMillis() + "_" + fileName;

        Path filePath = uploadPath.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), filePath);

        String extractedText;

        if (lowerCaseName.endsWith(".pdf")) {
            extractedText = extractPdfText(filePath);
        } else {
            extractedText = extractDocxText(filePath);
        }

        Resume resume = new Resume();

        resume.setFileName(fileName);
        resume.setFilePath(filePath.toString());
        resume.setUserEmail(userEmail);
        resume.setUploadedAt(LocalDateTime.now());
        resume.setExtractedText(extractedText);

        return resumeRepository.save(resume);
    }

    // Send resume text to Python Resume Analysis Agent
    public Object analyzeResume(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found")
                );

        if (resume.getExtractedText() == null
                || resume.getExtractedText().isBlank()) {

            throw new RuntimeException(
                    "No extracted text found in resume"
            );
        }

        return aiService.analyzeResume(
                resume.getExtractedText()
        );
    }

    // Extract text from PDF
    private String extractPdfText(Path filePath)
            throws IOException {

        byte[] pdfBytes = Files.readAllBytes(filePath);

        try (var document = Loader.loadPDF(pdfBytes)) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);
        }
    }

    // Extract text from DOCX
    private String extractDocxText(Path filePath)
            throws IOException {

        try (
                InputStream inputStream =
                        Files.newInputStream(filePath);

                XWPFDocument document =
                        new XWPFDocument(inputStream);

                XWPFWordExtractor extractor =
                        new XWPFWordExtractor(document)
        ) {

            return extractor.getText();
        }
    }
}