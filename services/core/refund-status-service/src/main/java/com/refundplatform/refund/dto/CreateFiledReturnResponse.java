package com.refundplatform.refund.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateFiledReturnResponse(
        UUID taxReturnId,
        Integer taxYear,
        Instant filedAt,
        BigDecimal refundAmount,
        String filingMethod,
        String externalRefundId,
        String status
) {
}
