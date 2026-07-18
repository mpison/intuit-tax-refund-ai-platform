package com.refundplatform.refund.infrastructure.persistence;

import com.refundplatform.refund.domain.RefundStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refund_statuses")
public class RefundStatusEntity {
    @Id
    @Column(name = "refund_status_id")
    private UUID refundStatusId;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tax_return_id", unique = true)
    private TaxReturnEntity taxReturn;
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private RefundStatus currentStatus;
    @Column(name = "last_checked_at", nullable = false)
    private Instant lastCheckedAt;

    protected RefundStatusEntity() {
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
}
