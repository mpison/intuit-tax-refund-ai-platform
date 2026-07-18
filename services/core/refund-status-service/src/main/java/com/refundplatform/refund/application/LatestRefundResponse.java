package com.refundplatform.refund.application;

import com.refundplatform.refund.domain.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LatestRefundResponse(UUID taxReturnId, String customerName, Integer taxYear, BigDecimal refundAmount,
                                   RefundStatus status, Instant filedAt, Instant lastCheckedAt, String guidance) {
}
