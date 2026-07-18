package com.refundplatform.refund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refundplatform.refund.model.RefundStatusHistoryEntity;

import java.util.UUID;

public interface RefundStatusHistoryRepository
        extends JpaRepository<
                RefundStatusHistoryEntity,
                UUID
        > {
}
