package com.refundplatform.policy.dto;

public record PolicyIngestionResponse(
        String policyDocumentId,
        String fileName,
        int chunkCount,
        int embeddingCount
) {
}
