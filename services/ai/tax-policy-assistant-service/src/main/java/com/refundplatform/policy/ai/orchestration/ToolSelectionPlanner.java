package com.refundplatform.policy.ai.orchestration;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ToolSelectionPlanner {

    public static final String CUSTOMER_TOOL =
            "get_customer_by_identity";

    public static final String LATEST_REFUND_TOOL =
            "get_latest_refund_by_identity";

    public static final String PREDICTION_TOOL =
            "predict_refund_date";

    private final ToolRegistry toolRegistry;

    public ToolSelectionPlanner(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    public AiOrchestrationPlan plan(
            QuestionIntentResult intentResult) {

        return switch (intentResult.intent()) {
            case POLICY_ONLY -> new AiOrchestrationPlan(
                    intentResult,
                    true,
                    false,
                    List.of(),
                    true,
                    List.of()
            );

            case ACCOUNT_ONLY -> accountPlan(
                    intentResult,
                    false
            );

            case ACCOUNT_AND_POLICY -> accountPlan(
                    intentResult,
                    true
            );

            case UNKNOWN -> new AiOrchestrationPlan(
                    intentResult,
                    true,
                    false,
                    List.of(),
                    true,
                    List.of(
                            "Unknown intent defaulted to policy RAG."
                    )
            );
        };
    }

    private AiOrchestrationPlan accountPlan(
            QuestionIntentResult intentResult,
            boolean useRag) {

        List<PlannedTool> plannedTools = List.of(
                tool(
                        CUSTOMER_TOOL,
                        true,
                        "Resolve the customer profile."
                ),
                tool(
                        LATEST_REFUND_TOOL,
                        true,
                        "Retrieve the latest return and refund status."
                ),
                tool(
                        PREDICTION_TOOL,
                        false,
                        "Estimate an expected refund date."
                )
        );

        boolean allRequiredToolsAvailable =
                plannedTools.stream()
                        .filter(PlannedTool::required)
                        .allMatch(PlannedTool::available);

        List<String> warnings = new ArrayList<>();

        if (!allRequiredToolsAvailable) {
            warnings.add(
                    "One or more required MCP tools are unavailable."
            );
        }

        return new AiOrchestrationPlan(
                intentResult,
                useRag,
                true,
                plannedTools,
                allRequiredToolsAvailable,
                warnings
        );
    }

    private PlannedTool tool(
            String logicalToolName,
            boolean required,
            String purpose) {

        return new PlannedTool(
                logicalToolName,
                toolRegistry.hasTool(logicalToolName),
                required,
                purpose
        );
    }
}
