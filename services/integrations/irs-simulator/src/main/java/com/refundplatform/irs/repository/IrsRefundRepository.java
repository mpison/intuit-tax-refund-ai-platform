package com.refundplatform.irs.repository;

import com.refundplatform.irs.dto.IrsRefundRecord;
import com.refundplatform.irs.model.IrsRefundStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class IrsRefundRepository {

    private final JdbcTemplate jdbcTemplate;

    public IrsRefundRepository(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }

    public Optional<IrsRefundRecord> findById(
            String externalRefundId) {

        List<IrsRefundRecord> records =
                jdbcTemplate.query(
                        """
                        SELECT
                            external_refund_id,
                            status,
                            official_refund_date,
                            updated_at
                        FROM irs_refund_records
                        WHERE external_refund_id = ?
                        """,
                        (resultSet, rowNumber) ->
                                mapRecord(
                                        resultSet.getString(
                                                "external_refund_id"
                                        ),
                                        resultSet.getString(
                                                "status"
                                        ),
                                        resultSet.getDate(
                                                "official_refund_date"
                                        ),
                                        resultSet.getTimestamp(
                                                "updated_at"
                                        )
                                ),
                        externalRefundId
                );

        return records.stream()
                .findFirst();
    }

    public List<IrsRefundRecord> findAll() {

        return jdbcTemplate.query(
                """
                SELECT
                    external_refund_id,
                    status,
                    official_refund_date,
                    updated_at
                FROM irs_refund_records
                ORDER BY updated_at DESC, external_refund_id
                """,
                (resultSet, rowNumber) ->
                        mapRecord(
                                resultSet.getString(
                                        "external_refund_id"
                                ),
                                resultSet.getString(
                                        "status"
                                ),
                                resultSet.getDate(
                                        "official_refund_date"
                                ),
                                resultSet.getTimestamp(
                                        "updated_at"
                                )
                        )
        );
    }

    public IrsRefundRecord save(
            String externalRefundId,
            IrsRefundStatus status,
            LocalDate officialRefundDate) {

        Instant updatedAt =
                Instant.now();

        jdbcTemplate.update(
                """
                INSERT INTO irs_refund_records (
                    external_refund_id,
                    status,
                    official_refund_date,
                    updated_at
                )
                VALUES (?, ?, ?, ?)
                ON CONFLICT (external_refund_id)
                DO UPDATE SET
                    status = EXCLUDED.status,
                    official_refund_date =
                        EXCLUDED.official_refund_date,
                    updated_at = EXCLUDED.updated_at
                """,
                externalRefundId,
                status.name(),
                officialRefundDate == null
                        ? null
                        : Date.valueOf(
                                officialRefundDate
                        ),
                Timestamp.from(
                        updatedAt
                )
        );

        return new IrsRefundRecord(
                externalRefundId,
                status,
                officialRefundDate,
                updatedAt
        );
    }

    public boolean deleteById(
            String externalRefundId) {

        return jdbcTemplate.update(
                """
                DELETE FROM irs_refund_records
                WHERE external_refund_id = ?
                """,
                externalRefundId
        ) > 0;
    }

    private IrsRefundRecord mapRecord(
            String externalRefundId,
            String status,
            Date officialRefundDate,
            Timestamp updatedAt) {

        return new IrsRefundRecord(
                externalRefundId,
                IrsRefundStatus.valueOf(
                        status
                ),
                officialRefundDate == null
                        ? null
                        : officialRefundDate.toLocalDate(),
                updatedAt == null
                        ? null
                        : updatedAt.toInstant()
        );
    }
}
