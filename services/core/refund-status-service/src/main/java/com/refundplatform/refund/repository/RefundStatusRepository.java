package com.refundplatform.refund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refundplatform.refund.model.RefundStatusEntity;

import java.util.*;

public interface RefundStatusRepository extends JpaRepository<RefundStatusEntity, UUID> {
    Optional<RefundStatusEntity> findByTaxReturnTaxReturnId(UUID taxReturnId);
}
