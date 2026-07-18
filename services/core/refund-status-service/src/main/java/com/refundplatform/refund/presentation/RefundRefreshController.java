package com.refundplatform.refund.presentation;

import com.refundplatform.refund.application.RefreshRefundResponse;
import com.refundplatform.refund.application.RefreshRefundUseCase;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundRefreshController {

    private final RefreshRefundUseCase
            refreshRefundUseCase;

    public RefundRefreshController(
            RefreshRefundUseCase refreshRefundUseCase) {

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
