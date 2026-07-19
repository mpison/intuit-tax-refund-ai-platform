package com.refundplatform.refund.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.refundplatform.refund.model.RefundStatusEntity;

@Repository
public interface SpringDataRefundStatusRepository
        extends JpaRepository<RefundStatusEntity, UUID> {

    Optional<RefundStatusEntity>
    findByTaxReturnTaxReturnId(
            UUID taxReturnId);
}