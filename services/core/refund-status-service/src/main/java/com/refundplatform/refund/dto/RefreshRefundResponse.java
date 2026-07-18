package com.refundplatform.refund.dto;

import java.time.Instant;
import java.util.UUID;

import com.refundplatform.refund.model.RefundStatus;

public record RefreshRefundResponse(

        UUID taxReturnId,

        RefundStatus previousStatus,

        RefundStatus currentStatus,

        Instant lastExternalSyncAt,

        String source
) {
}
