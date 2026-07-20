package com.refundplatform.policy.ai.orchestration;

import java.util.List;

public record QuestionIntentResult(
        QuestionIntent intent,
        double confidence,
        List<String> matchedSignals
) {
    public QuestionIntentResult {
        matchedSignals = matchedSignals == null
                ? List.of()
                : List.copyOf(matchedSignals);
    }
}
