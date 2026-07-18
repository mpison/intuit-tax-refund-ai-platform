package com.refundplatform.policy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient.Builder chatClientBuilder(
            ChatModel chatModel) {

        return ChatClient.builder(
                chatModel
        );
    }
}
