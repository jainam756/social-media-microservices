package com.example.aiservice.Controller;

import com.example.aiservice.model.AIRequest;
import com.example.aiservice.model.AIResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiContoller {
    private final ChatClient chatClient;
    @Autowired
    public GeminiContoller(ChatClient chatClient){
        this.chatClient=chatClient;
    }
    @PostMapping("/generate")
    public AIResponse generatePost(@RequestBody AIRequest request) {
        String prompt = "Given this idea: \"" + request.getDescription() +
                "\", suggest a social media post title and a short content body.";

        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

        String aiReply = response.getResult().getOutput().getText();

        // Simple splitting logic (you can improve this)
        String[] lines = aiReply.split("\n", 2);
        return new AIResponse(lines[0], lines.length > 1 ? lines[1] : "");
    }
}
