package com.refundplatform.refund.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByExternalIdentityId(String externalIdentityId);
}
