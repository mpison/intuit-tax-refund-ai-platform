package com.refundplatform.refund.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "app_users")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "external_identity_id", nullable = false, unique = true)
    private String externalIdentityId;
    @Column(name = "display_name", nullable = false)
    private String displayName;

    protected UserEntity() {
    }

    public UUID getUserId() {
        return userId;
    }

    public String getExternalIdentityId() {
        return externalIdentityId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
