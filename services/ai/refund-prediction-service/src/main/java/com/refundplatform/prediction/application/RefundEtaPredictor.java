package com.refundplatform.prediction.application;

public interface RefundEtaPredictor {
    RefundPredictionResponse predict(RefundPredictionRequest request);
}
