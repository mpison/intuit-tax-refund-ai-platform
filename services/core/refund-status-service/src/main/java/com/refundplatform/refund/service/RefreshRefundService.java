package com.refundplatform.refund.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refundplatform.refund.client.IrsSimulatorClient;
import com.refundplatform.refund.dto.IrsRefundResponse;
import com.refundplatform.refund.dto.RefreshRefundResponse;
import com.refundplatform.refund.exception.ResourceNotFoundException;
import com.refundplatform.refund.model.RefundStatus;
import com.refundplatform.refund.model.RefundStatusEntity;
import com.refundplatform.refund.model.RefundStatusHistoryEntity;
import com.refundplatform.refund.model.TaxReturnEntity;
import com.refundplatform.refund.repository.RefundStatusRepository;
import com.refundplatform.refund.repository.RefundStatusHistoryRepository;
import com.refundplatform.refund.repository.TaxReturnRepository;

@Service
public class RefreshRefundService {

    private final TaxReturnRepository
            taxReturnRepository;

    private final RefundStatusRepository
            refundStatusRepository;

    private final RefundStatusHistoryRepository
            refundStatusHistoryRepository;

    private final IrsSimulatorClient
            irsSimulatorClient;

    public RefreshRefundService(
            TaxReturnRepository taxReturnRepository,
            RefundStatusRepository refundStatusRepository,
            RefundStatusHistoryRepository refundStatusHistoryRepository,
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
