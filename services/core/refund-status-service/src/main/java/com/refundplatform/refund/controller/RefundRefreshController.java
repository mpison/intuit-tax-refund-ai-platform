package com.refundplatform.refund.controller;

import com.refundplatform.refund.dto.RefreshRefundResponse;
import com.refundplatform.refund.service.RefreshRefundService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundRefreshController {

    private final RefreshRefundService
            refreshRefundUseCase;

    public RefundRefreshController(
            RefreshRefundService refreshRefundUseCase) {

        this.refreshRefundUseCase =
                refreshRefundUseCase;
    }

    @PostMapping("/{taxReturnId}/refresh")
    public RefreshRefundResponse refresh(
            @PathVariable
            UUID taxReturnId) {

        return refreshRefundUseCase.execute(
                taxReturnId
        );
    }
}
