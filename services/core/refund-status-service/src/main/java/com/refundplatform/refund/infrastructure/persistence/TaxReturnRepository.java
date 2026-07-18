package com.refundplatform.refund.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface TaxReturnRepository extends JpaRepository<TaxReturnEntity, UUID> {
    Optional<TaxReturnEntity> findFirstByUserUserIdOrderByFiledAtDesc(UUID userId);
}
