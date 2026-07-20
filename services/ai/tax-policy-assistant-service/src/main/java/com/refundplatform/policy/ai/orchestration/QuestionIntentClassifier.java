package com.refundplatform.policy.ai.orchestration;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class QuestionIntentClassifier {

    private static final Set<String> ACCOUNT_SIGNALS = Set.of(
            "my refund",
            "my return",
            "my status",
            "my filing",
            "my account",
            "my tax return",
            "when will i",
            "when should i",
            "how much is my",
            "where is my",
            "why is my",
            "did i file",
            "what did i file",
            "expected refund date"
    );

    private static final Set<String> POLICY_SIGNALS = Set.of(
            "policy",
            "normally",
            "standard",
            "usually",
            "generally",
            "what does",
            "how long do refunds",
            "how long does a refund",
            "processing time",
            "amended return",
            "refund rule",
            "refund rules"
    );

    public QuestionIntentResult classify(String userQuestion) {
        if (userQuestion == null || userQuestion.isBlank()) {
            return new QuestionIntentResult(
                    QuestionIntent.UNKNOWN,
                    0.0,
                    List.of()
            );
        }

        String normalizedQuestion =
                userQuestion.trim().toLowerCase(Locale.ROOT);

        List<String> matchedSignals = new ArrayList<>();

        int accountScore = score(
                normalizedQuestion,
                ACCOUNT_SIGNALS,
                matchedSignals,
                "account:"
        );

        int policyScore = score(
                normalizedQuestion,
                POLICY_SIGNALS,
                matchedSignals,
                "policy:"
        );

        if (accountScore > 0 && policyScore > 0) {
            return new QuestionIntentResult(
                    QuestionIntent.ACCOUNT_AND_POLICY,
                    confidence(accountScore + policyScore),
                    matchedSignals
            );
        }

        if (accountScore > 0) {
            return new QuestionIntentResult(
                    QuestionIntent.ACCOUNT_ONLY,
                    confidence(accountScore),
                    matchedSignals
            );
        }

        if (policyScore > 0) {
            return new QuestionIntentResult(
                    QuestionIntent.POLICY_ONLY,
                    confidence(policyScore),
                    matchedSignals
            );
        }

        return new QuestionIntentResult(
                QuestionIntent.POLICY_ONLY,
                0.55,
                List.of("default:policy-fallback")
        );
    }

    private int score(
            String normalizedQuestion,
            Set<String> signals,
            List<String> matchedSignals,
            String prefix) {

        int score = 0;

        for (String signal : signals) {
            if (normalizedQuestion.contains(signal)) {
                score++;
                matchedSignals.add(prefix + signal);
            }
        }

        return score;
    }

    private double confidence(int score) {
        return Math.min(0.95, 0.60 + (score * 0.10));
    }
}
