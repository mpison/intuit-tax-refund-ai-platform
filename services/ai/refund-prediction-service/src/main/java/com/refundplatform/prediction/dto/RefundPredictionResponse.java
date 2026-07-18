package com.refundplatform.prediction.dto;

import java.time.LocalDate;

public record RefundPredictionResponse(
        LocalDate predictedRefundDate,
        long estimatedDaysRemaining,
        double confidenceScore,
        String modelVersion,
        String explanation
) {
}
