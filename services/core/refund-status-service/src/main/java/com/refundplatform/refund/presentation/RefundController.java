package com.refundplatform.refund.presentation;

import com.refundplatform.refund.application.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundController {
    private final GetLatestRefundUseCase useCase;

    public RefundController(GetLatestRefundUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/latest")
    public LatestRefundResponse latest(@AuthenticationPrincipal Jwt jwt) {
        return useCase.execute(jwt.getSubject());
    }
}
