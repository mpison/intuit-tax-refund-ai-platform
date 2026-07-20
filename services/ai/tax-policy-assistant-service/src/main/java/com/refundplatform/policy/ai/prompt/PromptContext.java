package com.refundplatform.policy.ai.prompt;

public record PromptContext(
        String systemInstructions,
        String policyContext,
        String accountContext,
        String userQuestion
) {
}
