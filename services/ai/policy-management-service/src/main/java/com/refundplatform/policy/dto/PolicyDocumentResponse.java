package com.refundplatform.policy.dto;

import java.time.Instant;
import java.util.UUID;

public record PolicyDocumentResponse(
        UUID policyDocumentId,
        String fileName,
        String contentType,
        long fileSize,
        String status,
        int chunkCount,
        int embeddingCount,
        Instant uploadedAt,
        Instant lastIngestedAt,
        String errorMessage
) {
}
