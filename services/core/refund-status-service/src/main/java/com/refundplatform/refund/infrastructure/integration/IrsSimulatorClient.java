package com.refundplatform.refund.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IrsSimulatorClient {

    private final RestClient restClient;

    public IrsSimulatorClient(
            RestClient.Builder restClientBuilder,
            @Value("${integrations.irs-simulator.base-url}")
            String baseUrl) {

        this.restClient =
                restClientBuilder
                        .baseUrl(
                                baseUrl
                        )
                        .build();
    }

    public IrsRefundResponse getRefund(
            String externalRefundId) {

        return restClient
                .get()
                .uri(
                        "/api/v1/irs/refunds/{externalRefundId}",
                        externalRefundId
                )
                .retrieve()
                .body(
                        IrsRefundResponse.class
                );
    }
}
