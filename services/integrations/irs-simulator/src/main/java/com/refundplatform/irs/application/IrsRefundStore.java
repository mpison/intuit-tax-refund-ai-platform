package com.refundplatform.irs.application;

import com.refundplatform.irs.domain.IrsRefundStatus;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IrsRefundStore {

    private final Map<String, IrsRefundRecord> records =
            new ConcurrentHashMap<>();

    public IrsRefundStore() {

        records.put(
                "IRS-DEMO-2025-0001",
                new IrsRefundRecord(
                        "IRS-DEMO-2025-0001",
                        IrsRefundStatus.PROCESSING,
                        null,
                        Instant.now()
                )
        );
    }

    public IrsRefundRecord find(
            String externalRefundId) {

        IrsRefundRecord record =
                records.get(
                        externalRefundId
                );

        if (record == null) {

            throw new IllegalArgumentException(
                    "IRS refund record was not found: "
                            + externalRefundId
            );
        }

        return record;
    }

    public IrsRefundRecord update(
            String externalRefundId,
            IrsRefundStatus status,
            LocalDate officialRefundDate) {

        IrsRefundRecord record =
                new IrsRefundRecord(
                        externalRefundId,
                        status,
                        officialRefundDate,
                        Instant.now()
                );

        records.put(
                externalRefundId,
                record
        );

        return record;
    }
}
