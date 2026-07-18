package com.refundplatform.refund.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.refundplatform.refund.model.RefundStatus;

public record RefundPredictionRequest(
        Integer taxYear,
        Instant filedAt,
        RefundStatus currentStatus,
        BigDecimal refundAmount,
        Instant lastExternalSyncAt
) {
}
