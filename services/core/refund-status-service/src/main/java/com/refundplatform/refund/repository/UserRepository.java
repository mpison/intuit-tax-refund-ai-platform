package com.refundplatform.refund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refundplatform.refund.model.UserEntity;

import java.util.*;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByExternalIdentityId(String externalIdentityId);
}
