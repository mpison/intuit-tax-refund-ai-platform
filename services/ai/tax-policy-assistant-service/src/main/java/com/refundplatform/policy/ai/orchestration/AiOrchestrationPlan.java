package com.refundplatform.policy.ai.orchestration;

import java.util.List;

public record AiOrchestrationPlan(
        QuestionIntentResult intentResult,
        boolean useRag,
        boolean useMcp,
        List<PlannedTool> tools,
        boolean executable,
        List<String> warnings
) {
    public AiOrchestrationPlan {
        tools = tools == null ? List.of() : List.copyOf(tools);
        warnings = warnings == null ? List.of() : List.copyOf(warnings);
    }
}
