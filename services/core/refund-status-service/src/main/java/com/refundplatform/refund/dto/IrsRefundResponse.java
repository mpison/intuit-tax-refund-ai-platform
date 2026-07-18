package com.refundplatform.refund.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.refundplatform.refund.model.RefundStatus;

public record IrsRefundResponse(

        String externalRefundId,

        RefundStatus status,

        LocalDate officialRefundDate,

        Instant updatedAt
) {
}
