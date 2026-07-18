package com.refundplatform.prediction.application;

import com.refundplatform.prediction.domain.RefundStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

public record RefundPredictionRequest(
        @NotNull Integer taxYear,
        @NotNull Instant filedAt,
        @NotNull RefundStatus currentStatus,
        @NotNull @PositiveOrZero BigDecimal refundAmount,
        Instant lastExternalSyncAt
) {
}
