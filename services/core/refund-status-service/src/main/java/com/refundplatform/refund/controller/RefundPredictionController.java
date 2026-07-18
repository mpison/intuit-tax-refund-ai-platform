package com.refundplatform.refund.controller;

import com.refundplatform.refund.dto.RefundPredictionResponse;
import com.refundplatform.refund.service.GetRefundPredictionService;

import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundPredictionController {

    private final GetRefundPredictionService useCase;

    public RefundPredictionController(GetRefundPredictionService useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{taxReturnId}/prediction")
    public RefundPredictionResponse getPrediction(@PathVariable UUID taxReturnId) {
        return useCase.execute(taxReturnId);
    }
}
