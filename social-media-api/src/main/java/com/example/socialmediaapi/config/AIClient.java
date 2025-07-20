package com.example.socialmediaapi.config;

import com.example.socialmediaapi.model.AIRequest;
import com.example.socialmediaapi.model.AIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AIClient {
    @PostMapping("/generate")
    AIResponse generate(@RequestBody AIRequest request);
}
