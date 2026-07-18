package com.refundplatform.refund.application;

import com.refundplatform.refund.domain.RefundStatus;
import com.refundplatform.refund.infrastructure.integration.IrsRefundResponse;
import com.refundplatform.refund.infrastructure.integration.IrsSimulatorClient;
import com.refundplatform.refund.infrastructure.persistence.RefundStatusEntity;
import com.refundplatform.refund.infrastructure.persistence.RefundStatusHistoryEntity;
import com.refundplatform.refund.infrastructure.persistence.SpringDataRefundStatusHistoryRepository;
import com.refundplatform.refund.infrastructure.persistence.SpringDataRefundStatusRepository;
import com.refundplatform.refund.infrastructure.persistence.SpringDataTaxReturnRepository;
import com.refundplatform.refund.infrastructure.persistence.TaxReturnEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshRefundUseCase {

    private final SpringDataTaxReturnRepository
            taxReturnRepository;

    private final SpringDataRefundStatusRepository
            refundStatusRepository;

    private final SpringDataRefundStatusHistoryRepository
            refundStatusHistoryRepository;

    private final IrsSimulatorClient
            irsSimulatorClient;

    public RefreshRefundUseCase(
            SpringDataTaxReturnRepository taxReturnRepository,
            SpringDataRefundStatusRepository refundStatusRepository,
            SpringDataRefundStatusHistoryRepository refundStatusHistoryRepository,
            IrsSimulatorClient irsSimulatorClient) {

        this.taxReturnRepository =
                taxReturnRepository;

        this.refundStatusRepository =
                refundStatusRepository;

        this.refundStatusHistoryRepository =
                refundStatusHistoryRepository;

        this.irsSimulatorClient =
                irsSimulatorClient;
    }

    @Transactional
    public RefreshRefundResponse execute(
            UUID taxReturnId) {

        TaxReturnEntity taxReturn =
                taxReturnRepository
                        .findById(
                                taxReturnId
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Tax return was not found"
                                        )
                        );

        RefundStatusEntity refundStatus =
                refundStatusRepository
                        .findByTaxReturnTaxReturnId(
                                taxReturnId
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Refund status was not found"
                                        )
                        );

        String externalRefundId =
                taxReturn.getExternalRefundId();

        IrsRefundResponse irsRefund =
                irsSimulatorClient.getRefund(
                        externalRefundId
                );

        RefundStatus previousStatus =
                refundStatus.getCurrentStatus();

        Instant syncTime =
                Instant.now();

        refundStatus.updateFromExternalSource(
                irsRefund.status(),
                syncTime,
                "IRS_SIMULATOR"
        );

        refundStatusRepository.save(
                refundStatus
        );

        if (previousStatus != irsRefund.status()) {

            refundStatusHistoryRepository.save(
                    new RefundStatusHistoryEntity(
                            UUID.randomUUID(),
                            taxReturnId,
                            previousStatus,
                            irsRefund.status(),
                            "IRS_SIMULATOR",
                            syncTime
                    )
            );
        }

        return new RefreshRefundResponse(
                taxReturnId,
                previousStatus,
                irsRefund.status(),
                syncTime,
                "IRS_SIMULATOR"
        );
    }
}
