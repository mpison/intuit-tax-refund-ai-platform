package com.refundplatform.refund.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RefundPredictionClient {

    private final RestClient restClient;

    public RefundPredictionClient(
            RestClient.Builder restClientBuilder,
            @Value("${integrations.refund-prediction.base-url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public RefundPredictionResponse predict(RefundPredictionRequest request) {
        return restClient.post()
                .uri("/api/v1/predictions/refund-eta")
                .body(request)
                .retrieve()
                .body(RefundPredictionResponse.class);
    }
}
