package com.refundplatform.irs.dto;

import java.time.LocalDate;

import com.refundplatform.irs.model.IrsRefundStatus;

public record UpdateIrsRefundRequest(

        IrsRefundStatus status,

        LocalDate officialRefundDate
) {
}
