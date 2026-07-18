package com.refundplatform.irs.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.refundplatform.irs.model.IrsRefundStatus;

public record IrsRefundRecord(

        String externalRefundId,

        IrsRefundStatus status,

        LocalDate officialRefundDate,

        Instant updatedAt
) {
}
