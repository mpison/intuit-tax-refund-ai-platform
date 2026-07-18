package com.refundplatform.prediction.service;

import com.refundplatform.prediction.dto.RefundPredictionRequest;
import com.refundplatform.prediction.dto.RefundPredictionResponse;

public interface RefundEtaPredictor {
    RefundPredictionResponse predict(RefundPredictionRequest request);
}
