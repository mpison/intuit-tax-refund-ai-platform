package com.refundplatform.refund.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateFiledReturnRequest(
        @NotNull Integer taxYear,
        @NotNull Instant filedAt,
        @NotNull @PositiveOrZero BigDecimal refundAmount,
        @NotNull String filingMethod,
        @Size(max = 100) String externalRefundId
) {
}
