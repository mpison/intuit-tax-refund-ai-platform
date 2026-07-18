package com.refundplatform.refund.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataRefundStatusHistoryRepository
        extends JpaRepository<
                RefundStatusHistoryEntity,
                UUID
        > {
}
