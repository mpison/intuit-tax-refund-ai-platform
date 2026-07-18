package com.refundplatform.prediction.controller;

import com.refundplatform.prediction.dto.RefundPredictionRequest;
import com.refundplatform.prediction.dto.RefundPredictionResponse;
import com.refundplatform.prediction.service.RefundEtaPredictor;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/predictions")
public class RefundPredictionController {

    private final RefundEtaPredictor refundEtaPredictor;

    public RefundPredictionController(RefundEtaPredictor refundEtaPredictor) {
        this.refundEtaPredictor = refundEtaPredictor;
    }

    @PostMapping("/refund-eta")
    public RefundPredictionResponse predictRefundEta(
            @Valid @RequestBody RefundPredictionRequest request) {
        return refundEtaPredictor.predict(request);
    }
}
