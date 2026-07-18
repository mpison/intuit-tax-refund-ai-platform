package com.refundplatform.prediction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

import com.refundplatform.prediction.model.RefundStatus;

public record RefundPredictionRequest(
        @NotNull Integer taxYear,
        @NotNull Instant filedAt,
        @NotNull RefundStatus currentStatus,
        @NotNull @PositiveOrZero BigDecimal refundAmount,
        Instant lastExternalSyncAt
) {
}
