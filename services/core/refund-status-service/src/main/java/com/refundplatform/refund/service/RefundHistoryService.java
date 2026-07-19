package com.refundplatform.refund.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.refundplatform.refund.dto.RefundHistoryEventResponse;
import com.refundplatform.refund.dto.RefundHistoryResponse;


@Service
public class RefundHistoryService {

    private final JdbcTemplate jdbcTemplate;

    public RefundHistoryService(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public RefundHistoryResponse getHistory(
            UUID taxReturnId,
            String externalIdentityId) {

        String currentStatus = jdbcTemplate.query(
                """
                SELECT rs.current_status
                FROM refund_statuses rs
                JOIN tax_returns tr
                    ON tr.tax_return_id = rs.tax_return_id
                JOIN app_users u
                    ON u.user_id = tr.user_id
                WHERE tr.tax_return_id = ?
                  AND u.external_identity_id = ?
                """,
                resultSet -> {
                    if (!resultSet.next()) {
                    	throw new ResponseStatusException(
                    	        HttpStatus.NOT_FOUND,
                    	        "Refund was not found for the authenticated user"
                    	);
                    }

                    return resultSet.getString(
                            "current_status"
                    );
                },
                taxReturnId,
                externalIdentityId
        );

        List<RefundHistoryEventResponse> events =
                jdbcTemplate.query(
                        """
                        SELECT
                            refund_status_history_id,
                            previous_status,
                            new_status,
                            source,
                            changed_at
                        FROM refund_status_history
                        WHERE tax_return_id = ?
                        ORDER BY changed_at DESC
                        """,
                        (resultSet, rowNumber) ->
                                new RefundHistoryEventResponse(
                                        resultSet.getObject(
                                                "refund_status_history_id",
                                                UUID.class
                                        ),
                                        resultSet.getString(
                                                "previous_status"
                                        ),
                                        resultSet.getString(
                                                "new_status"
                                        ),
                                        resultSet.getString(
                                                "source"
                                        ),
                                        toInstant(
                                                resultSet.getTimestamp(
                                                        "changed_at"
                                                )
                                        )
                                ),
                        taxReturnId
                );

        return new RefundHistoryResponse(
                taxReturnId,
                currentStatus,
                events
        );
    }

    private Instant toInstant(
            Timestamp timestamp) {

        return timestamp == null
                ? null
                : timestamp.toInstant();
    }
}
