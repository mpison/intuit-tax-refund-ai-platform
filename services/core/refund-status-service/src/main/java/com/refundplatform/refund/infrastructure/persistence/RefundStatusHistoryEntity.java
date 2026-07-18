package com.refundplatform.refund.infrastructure.persistence;

import com.refundplatform.refund.domain.RefundStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refund_status_history")
public class RefundStatusHistoryEntity {

    @Id
    @Column(
            name = "refund_status_history_id",
            nullable = false
    )
    private UUID refundStatusHistoryId;

    @Column(
            name = "tax_return_id",
            nullable = false
    )
    private UUID taxReturnId;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private RefundStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "new_status",
            nullable = false
    )
    private RefundStatus newStatus;

    @Column(
            name = "source",
            nullable = false
    )
    private String source;

    @Column(
            name = "changed_at",
            nullable = false
    )
    private Instant changedAt;

    protected RefundStatusHistoryEntity() {
    }

    public RefundStatusHistoryEntity(
            UUID refundStatusHistoryId,
            UUID taxReturnId,
            RefundStatus previousStatus,
            RefundStatus newStatus,
            String source,
            Instant changedAt) {

        this.refundStatusHistoryId =
                refundStatusHistoryId;

        this.taxReturnId =
                taxReturnId;

        this.previousStatus =
                previousStatus;

        this.newStatus =
                newStatus;

        this.source =
                source;

        this.changedAt =
                changedAt;
    }
}
