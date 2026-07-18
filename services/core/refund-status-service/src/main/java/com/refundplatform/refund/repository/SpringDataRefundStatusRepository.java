package com.refundplatform.refund.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataRefundStatusRepository
        extends JpaRepository<RefundStatusEntity, UUID> {

    Optional<RefundStatusEntity>
    findByTaxReturnTaxReturnId(
            UUID taxReturnId);
}