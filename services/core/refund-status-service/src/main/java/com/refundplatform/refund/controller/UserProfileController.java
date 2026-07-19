package com.refundplatform.refund.controller;

import com.refundplatform.refund.dto.UpdateUserProfileRequest;
import com.refundplatform.refund.dto.UserProfileResponse;
import com.refundplatform.refund.service.UserProfileService;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(
            UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public UserProfileResponse getCurrentUser(
            @AuthenticationPrincipal Jwt jwt) {
        return userProfileService.getOrCreate(jwt);
    }

    @PutMapping("/me")
    public UserProfileResponse updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        return userProfileService.update(jwt, request);
    }
}
