package com.refundplatform.policy.ai.context;

import java.util.Map;

public record AiExecutionContext(
        String conversationId,
        String externalIdentityId,
        String userQuestion,
        Map<String, Object> attributes
) {

    public AiExecutionContext {
        attributes = attributes == null
                ? Map.of()
                : Map.copyOf(attributes);
    }
}
