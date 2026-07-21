package com.refundplatform.policy.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ToolProviderConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ToolCallbackProvider.class)
    public ToolCallbackProvider defaultToolCallbackProvider() {
        return () -> new ToolCallback[0];
    }
}