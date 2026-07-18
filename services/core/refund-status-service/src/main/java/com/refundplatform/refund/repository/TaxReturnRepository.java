package com.refundplatform.refund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refundplatform.refund.model.TaxReturnEntity;

import java.util.*;

public interface TaxReturnRepository extends JpaRepository<TaxReturnEntity, UUID> {
    Optional<TaxReturnEntity> findFirstByUserUserIdOrderByFiledAtDesc(UUID userId);
}
