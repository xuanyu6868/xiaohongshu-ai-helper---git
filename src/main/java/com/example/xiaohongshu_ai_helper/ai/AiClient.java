package com.example.xiaohongshu_ai_helper.ai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class AiClient {

    private final ChatClient chatClient;

    public AiClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generate(String prompt) {
        Message userMessage = new UserMessage(prompt);
        Prompt chatPrompt = new Prompt(userMessage);
        return chatClient.call(chatPrompt).getResult().getOutput().getContent();
    }
}
