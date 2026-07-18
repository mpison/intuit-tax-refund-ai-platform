package com.refundplatform.refund.presentation;

import com.refundplatform.refund.application.GetRefundPredictionUseCase;
import com.refundplatform.refund.infrastructure.integration.RefundPredictionResponse;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundPredictionController {

    private final GetRefundPredictionUseCase useCase;

    public RefundPredictionController(GetRefundPredictionUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{taxReturnId}/prediction")
    public RefundPredictionResponse getPrediction(@PathVariable UUID taxReturnId) {
        return useCase.execute(taxReturnId);
    }
}
