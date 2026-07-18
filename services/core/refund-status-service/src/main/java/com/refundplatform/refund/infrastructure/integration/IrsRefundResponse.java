package com.refundplatform.refund.infrastructure.integration;

import com.refundplatform.refund.domain.RefundStatus;

import java.time.Instant;
import java.time.LocalDate;

public record IrsRefundResponse(

        String externalRefundId,

        RefundStatus status,

        LocalDate officialRefundDate,

        Instant updatedAt
) {
}
