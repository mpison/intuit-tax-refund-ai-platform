package com.refundplatform.refund.dto;

import java.util.List;
import java.util.UUID;

public record RefundHistoryResponse(
        UUID taxReturnId,
        String currentStatus,
        List<RefundHistoryEventResponse> events
) {
}
