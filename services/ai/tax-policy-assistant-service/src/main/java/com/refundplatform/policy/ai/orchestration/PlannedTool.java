package com.refundplatform.policy.ai.orchestration;

public record PlannedTool(
        String logicalToolName,
        boolean available,
        boolean required,
        String purpose
) {
}
