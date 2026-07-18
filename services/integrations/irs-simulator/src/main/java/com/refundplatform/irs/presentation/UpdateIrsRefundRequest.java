package com.refundplatform.irs.presentation;

import com.refundplatform.irs.domain.IrsRefundStatus;

import java.time.LocalDate;

public record UpdateIrsRefundRequest(

        IrsRefundStatus status,

        LocalDate officialRefundDate
) {
}
