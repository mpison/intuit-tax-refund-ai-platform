package com.refundplatform.refund.infrastructure.integration;

import java.time.LocalDate;

public record RefundPredictionResponse(
        LocalDate predictedRefundDate,
        long estimatedDaysRemaining,
        double confidenceScore,
        String modelVersion,
        String explanation
) {
}
