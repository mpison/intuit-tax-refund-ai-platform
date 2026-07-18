package com.refundplatform.refund.application;

import com.refundplatform.refund.infrastructure.integration.*;
import com.refundplatform.refund.infrastructure.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class GetRefundPredictionUseCase {

    private final SpringDataTaxReturnRepository taxReturnRepository;
    private final SpringDataRefundStatusRepository refundStatusRepository;
    private final RefundPredictionClient refundPredictionClient;

    public GetRefundPredictionUseCase(
            SpringDataTaxReturnRepository taxReturnRepository,
            SpringDataRefundStatusRepository refundStatusRepository,
            RefundPredictionClient refundPredictionClient) {
        this.taxReturnRepository = taxReturnRepository;
        this.refundStatusRepository = refundStatusRepository;
        this.refundPredictionClient = refundPredictionClient;
    }

    @Transactional(readOnly = true)
    public RefundPredictionResponse execute(UUID taxReturnId) {
        TaxReturnEntity taxReturn = taxReturnRepository.findById(taxReturnId)
                .orElseThrow(() -> new ResourceNotFoundException("Tax return was not found"));

        RefundStatusEntity refundStatus =
                refundStatusRepository.findByTaxReturnTaxReturnId(taxReturnId)
                        .orElseThrow(() -> new ResourceNotFoundException("Refund status was not found"));

        return refundPredictionClient.predict(
                new RefundPredictionRequest(
                        taxReturn.getTaxYear(),
                        taxReturn.getFiledAt(),
                        refundStatus.getCurrentStatus(),
                        taxReturn.getRefundAmount(),
                        refundStatus.getLastExternalSyncAt()
                )
        );
    }
}
