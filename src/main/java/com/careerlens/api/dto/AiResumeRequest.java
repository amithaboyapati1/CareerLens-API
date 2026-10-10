package com.careerlens.api.dto;

public class AiResumeRequest {

    private String text;

    public AiResumeRequest() {
    }

    public AiResumeRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
