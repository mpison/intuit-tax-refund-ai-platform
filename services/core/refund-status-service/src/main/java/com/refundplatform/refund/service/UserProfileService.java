package com.refundplatform.refund.service;

import com.refundplatform.refund.dto.UpdateUserProfileRequest;
import com.refundplatform.refund.dto.UserProfileResponse;
import com.refundplatform.refund.model.UserEntity;
import com.refundplatform.refund.repository.UserRepository;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfileResponse getOrCreate(
            Jwt jwt) {
        UserEntity user =
                userRepository
                        .findByExternalIdentityId(jwt.getSubject())
                        .orElseGet(() -> createFromJwt(jwt));

        synchronizeIdentityClaims(user, jwt);

        return toResponse(
                userRepository.save(user)
        );
    }

    @Transactional
    public UserProfileResponse update(
            Jwt jwt,
            UpdateUserProfileRequest request) {
        UserEntity user =
                userRepository
                        .findByExternalIdentityId(jwt.getSubject())
                        .orElseGet(() -> createFromJwt(jwt));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setDisplayName(request.displayName());
        user.setPhoneNumber(request.phoneNumber());
        user.setUpdatedAt(Instant.now());

        return toResponse(
                userRepository.save(user)
        );
    }

    private UserEntity createFromJwt(
            Jwt jwt) {
        Instant now = Instant.now();

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setExternalIdentityId(jwt.getSubject());
        user.setEmail(claim(jwt, "email"));
        user.setFirstName(claim(jwt, "given_name"));
        user.setLastName(claim(jwt, "family_name"));
        user.setDisplayName(resolveDisplayName(jwt));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return user;
    }

    private void synchronizeIdentityClaims(
            UserEntity user,
            Jwt jwt) {
        String email = claim(jwt, "email");

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        if (user.getFirstName() == null
                || user.getFirstName().isBlank()) {
            user.setFirstName(claim(jwt, "given_name"));
        }

        if (user.getLastName() == null
                || user.getLastName().isBlank()) {
            user.setLastName(claim(jwt, "family_name"));
        }

        if (user.getDisplayName() == null
                || user.getDisplayName().isBlank()) {
            user.setDisplayName(resolveDisplayName(jwt));
        }

        user.setUpdatedAt(Instant.now());
    }

    private String resolveDisplayName(
            Jwt jwt) {
        String name = claim(jwt, "name");

        if (name != null && !name.isBlank()) {
            return name;
        }

        String username =
                claim(jwt, "preferred_username");

        return username == null
                ? "Refund Platform User"
                : username;
    }

    private String claim(
            Jwt jwt,
            String claimName) {
        Object value =
                jwt.getClaims().get(claimName);

        return value == null
                ? null
                : String.valueOf(value);
    }

    private UserProfileResponse toResponse(
            UserEntity user) {
        return new UserProfileResponse(
                user.getUserId(),
                user.getExternalIdentityId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDisplayName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
