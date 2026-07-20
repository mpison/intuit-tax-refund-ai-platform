package com.refundplatform.policy.ai.orchestration;

public record ToolMetadata(
        String name,
        String description,
        String inputSchema,
        String providerType
) {
}
