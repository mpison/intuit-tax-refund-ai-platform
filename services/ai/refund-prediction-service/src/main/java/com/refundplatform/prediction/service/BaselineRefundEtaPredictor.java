package com.refundplatform.prediction.service;

import com.refundplatform.prediction.dto.RefundPredictionRequest;
import com.refundplatform.prediction.dto.RefundPredictionResponse;
import com.refundplatform.prediction.model.RefundStatus;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Component
public class BaselineRefundEtaPredictor implements RefundEtaPredictor {

    @Override
    public RefundPredictionResponse predict(RefundPredictionRequest request) {
        LocalDate filedDate = request.filedAt()
                .atZone(ZoneOffset.UTC)
                .toLocalDate();

        int expectedDays = expectedDays(request.currentStatus());
        LocalDate predictedDate = filedDate.plusDays(expectedDays);
        long daysRemaining = Math.max(
                0,
                ChronoUnit.DAYS.between(LocalDate.now(ZoneOffset.UTC), predictedDate)
        );

        return new RefundPredictionResponse(
                predictedDate,
                daysRemaining,
                confidence(request.currentStatus()),
                "baseline-eta-v1",
                "The baseline model estimates processing time from the current refund status."
        );
    }

    private int expectedDays(RefundStatus status) {
        return switch (status) {
            case FILED -> 28;
            case ACCEPTED -> 21;
            case PROCESSING -> 14;
            case ADDITIONAL_REVIEW -> 35;
            case ACTION_REQUIRED -> 45;
            case APPROVED -> 5;
            case REFUND_SENT -> 3;
            case REFUND_RECEIVED -> 0;
        };
    }

    private double confidence(RefundStatus status) {
        return switch (status) {
            case FILED -> 0.68;
            case ACCEPTED -> 0.76;
            case PROCESSING -> 0.82;
            case ADDITIONAL_REVIEW -> 0.55;
            case ACTION_REQUIRED -> 0.45;
            case APPROVED -> 0.93;
            case REFUND_SENT -> 0.97;
            case REFUND_RECEIVED -> 1.00;
        };
    }
}
