package com.refundplatform.policy.ai.orchestration;

import com.refundplatform.policy.ai.context.AiExecutionContext;

import org.springframework.stereotype.Service;

@Service
public class AiOrchestrator {

    private final QuestionIntentClassifier intentClassifier;
    private final ToolSelectionPlanner toolSelectionPlanner;

    public AiOrchestrator(
            QuestionIntentClassifier intentClassifier,
            ToolSelectionPlanner toolSelectionPlanner) {

        this.intentClassifier = intentClassifier;
        this.toolSelectionPlanner = toolSelectionPlanner;
    }

    public AiOrchestrationPlan plan(
            AiExecutionContext executionContext) {

        QuestionIntentResult intentResult =
                intentClassifier.classify(
                        executionContext.userQuestion()
                );

        return toolSelectionPlanner.plan(intentResult);
    }
}
