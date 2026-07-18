package com.refundplatform.policy.dto;

import java.util.List;

public record AssistantResponse(

        String answer,

        List<String> sources
) {
}
