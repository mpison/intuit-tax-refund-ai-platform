package com.refundplatform.policy.dto;

import jakarta.validation.constraints.NotBlank;

public record AssistantRequest(

        @NotBlank
        String conversationId,

        @NotBlank
        String question
) {
}
