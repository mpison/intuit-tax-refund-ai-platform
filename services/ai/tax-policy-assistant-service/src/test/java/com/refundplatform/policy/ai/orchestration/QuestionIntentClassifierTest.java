package com.refundplatform.policy.ai.orchestration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionIntentClassifierTest {

    private final QuestionIntentClassifier classifier =
            new QuestionIntentClassifier();

    @Test
    void classifiesGenericPolicyQuestion() {
        QuestionIntentResult result =
                classifier.classify(
                        "How long do refunds normally take?"
                );

        assertThat(result.intent())
                .isEqualTo(QuestionIntent.POLICY_ONLY);
    }

    @Test
    void classifiesPersonalRefundQuestion() {
        QuestionIntentResult result =
                classifier.classify(
                        "When will I receive my refund?"
                );

        assertThat(result.intent())
                .isEqualTo(QuestionIntent.ACCOUNT_ONLY);
    }

    @Test
    void classifiesMixedQuestion() {
        QuestionIntentResult result =
                classifier.classify(
                        "Why is my refund delayed under the standard policy?"
                );

        assertThat(result.intent())
                .isEqualTo(QuestionIntent.ACCOUNT_AND_POLICY);
    }
}
