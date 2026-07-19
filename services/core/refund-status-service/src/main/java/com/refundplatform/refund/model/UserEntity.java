package com.refundplatform.refund.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_users")
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(
            name = "external_identity_id",
            nullable = false,
            unique = true
    )
    private String externalIdentityId;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    @Column(
            name = "first_name",
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            length = 100
    )
    private String lastName;

    @Column(
            name = "display_name",
            length = 150
    )
    private String displayName;

    @Column(
            name = "phone_number",
            length = 32
    )
    private String phoneNumber;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(
            UUID userId) {

        this.userId = userId;
    }

    public String getExternalIdentityId() {
        return externalIdentityId;
    }

    public void setExternalIdentityId(
            String externalIdentityId) {

        this.externalIdentityId = externalIdentityId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email) {

        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(
            String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(
            String lastName) {

        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(
            String displayName) {

        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(
            String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            Instant createdAt) {

        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            Instant updatedAt) {

        this.updatedAt = updatedAt;
    }
}
