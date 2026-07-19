package com.refundplatform.refund.dto;

import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String externalIdentityId,
        String email,
        String firstName,
        String lastName,
        String displayName,
        String phoneNumber,
        Instant createdAt,
        Instant updatedAt
) {
}
