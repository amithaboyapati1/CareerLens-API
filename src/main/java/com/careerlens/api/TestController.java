package com.careerlens.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "CareerLens AI Backend is running! 🚀 Team: IDEALENS";
    }

    @GetMapping("/protected")
    public String protectedApi() {
        return "JWT Authentication successful! 🔐 Welcome to CareerLens AI.";
    }
}