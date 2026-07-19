package com.refundplatform.refund.controller;

import com.refundplatform.refund.dto.RefundHistoryResponse;
import com.refundplatform.refund.service.RefundHistoryService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundHistoryController {

    private final RefundHistoryService refundHistoryService;

    public RefundHistoryController(
            RefundHistoryService refundHistoryService) {

        this.refundHistoryService =
                refundHistoryService;
    }

    @GetMapping("/{taxReturnId}/history")
    public RefundHistoryResponse getHistory(
            @PathVariable
            UUID taxReturnId,

            @AuthenticationPrincipal
            Jwt jwt) {

        return refundHistoryService.getHistory(
                taxReturnId,
                jwt.getSubject()
        );
    }
}
