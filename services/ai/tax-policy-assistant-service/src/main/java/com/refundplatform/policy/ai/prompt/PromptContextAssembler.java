package com.refundplatform.policy.ai.prompt;

import com.refundplatform.policy.ai.context.AiExecutionContext;

import org.springframework.stereotype.Component;

@Component
public class PromptContextAssembler {

    public PromptContext assemblePolicyOnly(
            AiExecutionContext executionContext,
            String policyContext) {

        return new PromptContext(
                policySystemInstructions(),
                safe(policyContext),
                "",
                safe(executionContext.userQuestion())
        );
    }

    public PromptContext assembleAccountAware(
            AiExecutionContext executionContext,
            String policyContext,
            String accountContext) {

        return new PromptContext(
                accountAwareSystemInstructions(),
                safe(policyContext),
                safe(accountContext),
                safe(executionContext.userQuestion())
        );
    }

    private String policySystemInstructions() {

        return "Answer only from the supplied policy context. "
                + "If the context does not contain enough information, "
                + "state that the answer is unavailable. "
                + "Preserve source attribution when available.";
    }

    private String accountAwareSystemInstructions() {

        return "Answer using the supplied policy and authenticated "
                + "customer context. Never invent customer data, refund "
                + "dates, statuses, amounts, or confidence values. "
                + "Prefer official dates over predictions and predictions "
                + "over policy-only estimates.";
    }

    private String safe(
            String value) {

        return value == null ? "" : value;
    }
}
