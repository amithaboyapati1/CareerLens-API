package com.careerlens.api.service;

import com.careerlens.api.dto.AiResumeRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AiService {

    private final RestClient restClient;

    public AiService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://127.0.0.1:8000")
                .build();
    }

    public Map<String, Object> analyzeResume(String resumeText) {

        AiResumeRequest request =
                new AiResumeRequest(resumeText);

        return restClient.post()
                .uri("/api/agent/resume/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);
    }
}