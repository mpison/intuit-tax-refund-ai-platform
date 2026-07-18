package com.refundplatform.prediction.application;

import java.time.LocalDate;

public record RefundPredictionResponse(
        LocalDate predictedRefundDate,
        long estimatedDaysRemaining,
        double confidenceScore,
        String modelVersion,
        String explanation
) {
}
