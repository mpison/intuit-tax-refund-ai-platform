package com.refundplatform.policy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(
            CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:3200",
                        "http://127.0.0.1:3200"
                )
                .allowedMethods(
                        "GET",
                        "POST",
                        "DELETE",
                        "OPTIONS"
                )
                .allowedHeaders("*");
    }
}
