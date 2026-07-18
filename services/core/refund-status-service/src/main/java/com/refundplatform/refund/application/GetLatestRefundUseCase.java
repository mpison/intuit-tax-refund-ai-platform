package com.refundplatform.refund.application;

import com.refundplatform.refund.infrastructure.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetLatestRefundUseCase {
    private final UserRepository users;
    private final TaxReturnRepository returns;
    private final RefundStatusRepository statuses;

    public GetLatestRefundUseCase(UserRepository users, TaxReturnRepository returns, RefundStatusRepository statuses) {
        this.users = users;
        this.returns = returns;
        this.statuses = statuses;
    }

    @Transactional(readOnly = true)
    public LatestRefundResponse execute(String subject) {
        var user = users.findByExternalIdentityId(subject).orElseThrow(() -> new ResourceNotFoundException("Authenticated user is not registered"));
        var taxReturn = returns.findFirstByUserUserIdOrderByFiledAtDesc(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No filed tax return was found"));
        var status = statuses.findByTaxReturnTaxReturnId(taxReturn.getTaxReturnId()).orElseThrow(() -> new ResourceNotFoundException("Refund status was not found"));
        return new LatestRefundResponse(taxReturn.getTaxReturnId(), user.getDisplayName(), taxReturn.getTaxYear(), taxReturn.getRefundAmount(), status.getCurrentStatus(), taxReturn.getFiledAt(), status.getLastCheckedAt(), guidance(status));
    }

    private String guidance(RefundStatusEntity s) {
        return switch (s.getCurrentStatus()) {
            case FILED, ACCEPTED, PROCESSING -> "No action is required. We will continue checking for updates.";
            case ADDITIONAL_REVIEW -> "Your return requires additional review and may take longer.";
            case ACTION_REQUIRED -> "Please review the action requested on your account.";
            case APPROVED -> "Your refund has been approved.";
            case REFUND_SENT -> "Your refund has been sent. Check your bank account.";
            case REFUND_RECEIVED -> "Your refund is complete.";
        };
    }
}
