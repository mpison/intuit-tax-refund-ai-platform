package com.refundplatform.refund.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface RefundStatusRepository extends JpaRepository<RefundStatusEntity, UUID> {
    Optional<RefundStatusEntity> findByTaxReturnTaxReturnId(UUID taxReturnId);
}
