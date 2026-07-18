package com.refundplatform.refund.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.refundplatform.refund.model.RefundStatus;

public record LatestRefundResponse(UUID taxReturnId, String customerName, Integer taxYear, BigDecimal refundAmount,
                                   RefundStatus status, Instant filedAt, Instant lastCheckedAt, String guidance) {
}
