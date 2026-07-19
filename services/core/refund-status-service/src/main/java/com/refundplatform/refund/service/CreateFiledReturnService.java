package com.refundplatform.refund.service;

import com.refundplatform.refund.dto.CreateFiledReturnRequest;
import com.refundplatform.refund.dto.CreateFiledReturnResponse;
import com.refundplatform.refund.exception.ResourceNotFoundException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.sql.Timestamp;

@Service
public class CreateFiledReturnService {

    private final JdbcTemplate jdbcTemplate;

    public CreateFiledReturnService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public CreateFiledReturnResponse create(
            String externalIdentityId,
            CreateFiledReturnRequest request) {

        UUID userId = jdbcTemplate.query(
                """
                SELECT user_id
                FROM app_users
                WHERE external_identity_id = ?
                """,
                resultSet -> {
                    if (!resultSet.next()) {
                        throw new ResourceNotFoundException(
                                "Application user was not found"
                        );
                    }
                    return resultSet.getObject("user_id", UUID.class);
                },
                externalIdentityId
        );

        UUID taxReturnId = UUID.randomUUID();
        UUID refundStatusId = UUID.randomUUID();
        String externalRefundId =
                normalizeExternalRefundId(
                        request.externalRefundId(),
                        request.taxYear()
                );
        String filingMethod =
                request.filingMethod()
                        .trim()
                        .toUpperCase(Locale.ROOT);
        Instant currentTime = Instant.now();

        try {
        	jdbcTemplate.update(
        	        """
        	        INSERT INTO tax_returns (
        	            tax_return_id,
        	            user_id,
        	            tax_year,
        	            filed_at,
        	            refund_amount,
        	            external_refund_id,
        	            filing_method
        	        )
        	        VALUES (?, ?, ?, ?, ?, ?, ?)
        	        """,
        	        taxReturnId,
        	        userId,
        	        request.taxYear(),
        	        Timestamp.from(request.filedAt()),
        	        request.refundAmount(),
        	        externalRefundId,
        	        filingMethod
        	);
        }
        catch (DuplicateKeyException exception) {
            throw new IllegalArgumentException(
                    "The IRS reference number is already in use",
                    exception
            );
        }

        jdbcTemplate.update(
                """
                INSERT INTO refund_statuses (
                    refund_status_id,
                    tax_return_id,
                    current_status,
                    last_checked_at,
                    external_source
                )
                VALUES (?, ?, ?, ?, ?)
                """,
                refundStatusId,
                taxReturnId,
                "FILED",
                Timestamp.from(currentTime),
                "CUSTOMER_REGISTRATION"
        );

        return new CreateFiledReturnResponse(
                taxReturnId,
                request.taxYear(),
                request.filedAt(),
                request.refundAmount(),
                filingMethod,
                externalRefundId,
                "FILED"
        );
    }

    private String normalizeExternalRefundId(
            String externalRefundId,
            Integer taxYear) {

        if (externalRefundId != null && !externalRefundId.isBlank()) {
            return externalRefundId.trim();
        }

        return "IRS-DEMO-"
                + taxYear
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase(Locale.ROOT);
    }
}
