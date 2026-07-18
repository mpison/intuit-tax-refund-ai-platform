package com.refundplatform.irs.application;

import com.refundplatform.irs.domain.IrsRefundStatus;

import java.time.Instant;
import java.time.LocalDate;

public record IrsRefundRecord(

        String externalRefundId,

        IrsRefundStatus status,

        LocalDate officialRefundDate,

        Instant updatedAt
) {
}
