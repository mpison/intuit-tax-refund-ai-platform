package com.refundplatform.refund.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refundplatform.refund.client.RefundPredictionClient;
import com.refundplatform.refund.dto.RefundPredictionRequest;
import com.refundplatform.refund.dto.RefundPredictionResponse;
import com.refundplatform.refund.exception.ResourceNotFoundException;
import com.refundplatform.refund.model.RefundStatusEntity;
import com.refundplatform.refund.model.TaxReturnEntity;
import com.refundplatform.refund.repository.RefundStatusRepository;
import com.refundplatform.refund.repository.TaxReturnRepository;

@Service
public class GetRefundPredictionService {

    private final TaxReturnRepository taxReturnRepository;
    private final RefundStatusRepository refundStatusRepository;
    private final RefundPredictionClient refundPredictionClient;

    public GetRefundPredictionService(
            TaxReturnRepository taxReturnRepository,
            RefundStatusRepository refundStatusRepository,
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
