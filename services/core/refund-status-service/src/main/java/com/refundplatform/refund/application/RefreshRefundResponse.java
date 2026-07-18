package com.refundplatform.refund.application;

import com.refundplatform.refund.domain.RefundStatus;

import java.time.Instant;
import java.util.UUID;

public record RefreshRefundResponse(

        UUID taxReturnId,

        RefundStatus previousStatus,

        RefundStatus currentStatus,

        Instant lastExternalSyncAt,

        String source
) {
}
