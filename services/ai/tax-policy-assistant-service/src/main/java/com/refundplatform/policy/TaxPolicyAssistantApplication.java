package com.refundplatform.policy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaxPolicyAssistantApplication {

    public static void main(
            String[] arguments) {

        SpringApplication.run(
                TaxPolicyAssistantApplication.class,
                arguments
        );
    }
}
