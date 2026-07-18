package com.refundplatform.refund.infrastructure.persistence;

import com.refundplatform.refund.domain.RefundStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refund_statuses")
public class RefundStatusEntity {

    @Id
    @Column(name = "refund_status_id")
    private UUID refundStatusId;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "tax_return_id",
            unique = true
    )
    private TaxReturnEntity taxReturn;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private RefundStatus currentStatus;

    @Column(name = "last_checked_at")
    private Instant lastCheckedAt;

    @Column(name = "last_external_sync_at")
    private Instant lastExternalSyncAt;

    @Column(name = "external_source")
    private String externalSource;

    protected RefundStatusEntity() {
    }

    public UUID getRefundStatusId() {
        return refundStatusId;
    }

    public TaxReturnEntity getTaxReturn() {
        return taxReturn;
    }

    public RefundStatus getCurrentStatus() {
        return currentStatus;
    }

    public Instant getLastCheckedAt() {
        return lastCheckedAt;
    }

    public Instant getLastExternalSyncAt() {
        return lastExternalSyncAt;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void updateFromExternalSource(
            RefundStatus currentStatus,
            Instant syncTime,
            String externalSource) {

        this.currentStatus =
                currentStatus;

        this.lastCheckedAt =
                syncTime;

        this.lastExternalSyncAt =
                syncTime;

        this.externalSource =
                externalSource;
    }
}
