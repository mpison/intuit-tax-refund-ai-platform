package com.refundplatform.refund.repository;

import com.refundplatform.refund.model.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository
        extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByExternalIdentityId(
            String externalIdentityId
    );

    Optional<UserEntity> findByEmail(
            String email
    );
}
