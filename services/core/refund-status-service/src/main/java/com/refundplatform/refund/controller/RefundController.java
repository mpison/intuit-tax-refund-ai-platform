package com.refundplatform.refund.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.refundplatform.refund.dto.LatestRefundResponse;
import com.refundplatform.refund.service.GetLatestRefundService;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundController {
    private final GetLatestRefundService useCase;

    public RefundController(GetLatestRefundService useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/latest")
    public LatestRefundResponse latest(@AuthenticationPrincipal Jwt jwt) {
        return useCase.execute(jwt.getSubject());
    }
}
