package com.refundplatform.irs.dto;

import com.refundplatform.irs.model.IrsRefundStatus;

import java.time.LocalDate;

public record CreateIrsRefundRequest(
        String externalRefundId,
        IrsRefundStatus status,
        LocalDate officialRefundDate
) {
}
