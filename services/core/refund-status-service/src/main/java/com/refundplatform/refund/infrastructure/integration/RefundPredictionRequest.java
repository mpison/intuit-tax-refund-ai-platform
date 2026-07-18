package com.refundplatform.refund.infrastructure.integration;

import com.refundplatform.refund.domain.RefundStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record RefundPredictionRequest(
        Integer taxYear,
        Instant filedAt,
        RefundStatus currentStatus,
        BigDecimal refundAmount,
        Instant lastExternalSyncAt
) {
}
