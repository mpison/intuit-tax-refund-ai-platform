package com.refundplatform.refund.dto;

import java.time.Instant;
import java.util.UUID;

public record RefundHistoryEventResponse(
        UUID historyId,
        String previousStatus,
        String newStatus,
        String source,
        Instant changedAt
) {
}
