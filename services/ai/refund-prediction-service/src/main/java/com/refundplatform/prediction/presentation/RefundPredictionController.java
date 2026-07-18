package com.refundplatform.prediction.presentation;

import com.refundplatform.prediction.application.RefundEtaPredictor;
import com.refundplatform.prediction.application.RefundPredictionRequest;
import com.refundplatform.prediction.application.RefundPredictionResponse;
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
