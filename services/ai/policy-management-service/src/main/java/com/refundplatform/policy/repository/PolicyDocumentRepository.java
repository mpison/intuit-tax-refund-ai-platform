package com.refundplatform.policy.repository;

import com.refundplatform.policy.dto.PolicyDocumentResponse;
import com.refundplatform.policy.model.PolicyDocumentStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PolicyDocumentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PolicyDocumentRepository(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }

    public PolicyDocumentResponse insert(
            UUID policyDocumentId,
            String fileName,
            String contentType,
            long fileSize,
            String storagePath) {

        Instant now =
                Instant.now();

        jdbcTemplate.update(
                """
                INSERT INTO policy_documents (
                    policy_document_id,
                    file_name,
                    content_type,
                    file_size,
                    storage_path,
                    status,
                    chunk_count,
                    embedding_count,
                    uploaded_at,
                    updated_at
                )
                VALUES (?, ?, ?, ?, ?, ?, 0, 0, ?, ?)
                """,
                policyDocumentId,
                fileName,
                contentType,
                fileSize,
                storagePath,
                PolicyDocumentStatus.UPLOADED.name(),
                Timestamp.from(now),
                Timestamp.from(now)
        );

        return findById(
                policyDocumentId
        ).orElseThrow();
    }

    public List<PolicyDocumentResponse> findAll() {

        return jdbcTemplate.query(
                selectProjection()
                        + """
                        ORDER BY uploaded_at DESC
                        """,
                (resultSet, rowNumber) ->
                        map(resultSet)
        );
    }

    public Optional<PolicyDocumentResponse> findById(
            UUID policyDocumentId) {

        List<PolicyDocumentResponse> rows =
                jdbcTemplate.query(
                        selectProjection()
                                + """
                                WHERE policy_document_id = ?
                                """,
                        (resultSet, rowNumber) ->
                                map(resultSet),
                        policyDocumentId
                );

        return rows.stream()
                .findFirst();
    }

    public Optional<String> findStoragePath(
            UUID policyDocumentId) {

        List<String> rows =
                jdbcTemplate.query(
                        """
                        SELECT storage_path
                        FROM policy_documents
                        WHERE policy_document_id = ?
                        """,
                        (resultSet, rowNumber) ->
                                resultSet.getString(
                                        "storage_path"
                                ),
                        policyDocumentId
                );

        return rows.stream()
                .findFirst();
    }

    public void markProcessing(
            UUID policyDocumentId) {

        jdbcTemplate.update(
                """
                UPDATE policy_documents
                SET
                    status = ?,
                    error_message = NULL,
                    updated_at = NOW()
                WHERE policy_document_id = ?
                """,
                PolicyDocumentStatus.PROCESSING.name(),
                policyDocumentId
        );
    }

    public void markReady(
            UUID policyDocumentId,
            int chunkCount,
            int embeddingCount) {

        jdbcTemplate.update(
                """
                UPDATE policy_documents
                SET
                    status = ?,
                    chunk_count = ?,
                    embedding_count = ?,
                    last_ingested_at = NOW(),
                    error_message = NULL,
                    updated_at = NOW()
                WHERE policy_document_id = ?
                """,
                PolicyDocumentStatus.READY.name(),
                chunkCount,
                embeddingCount,
                policyDocumentId
        );
    }

    public void markFailed(
            UUID policyDocumentId,
            String errorMessage) {

        jdbcTemplate.update(
                """
                UPDATE policy_documents
                SET
                    status = ?,
                    error_message = ?,
                    updated_at = NOW()
                WHERE policy_document_id = ?
                """,
                PolicyDocumentStatus.FAILED.name(),
                errorMessage,
                policyDocumentId
        );
    }

    public boolean deleteById(
            UUID policyDocumentId) {

        return jdbcTemplate.update(
                """
                DELETE FROM policy_documents
                WHERE policy_document_id = ?
                """,
                policyDocumentId
        ) > 0;
    }

    private String selectProjection() {

        return """
                SELECT
                    policy_document_id,
                    file_name,
                    content_type,
                    file_size,
                    status,
                    chunk_count,
                    embedding_count,
                    uploaded_at,
                    last_ingested_at,
                    error_message
                FROM policy_documents
                """;
    }

    private PolicyDocumentResponse map(
            java.sql.ResultSet resultSet)
            throws java.sql.SQLException {

        Timestamp lastIngestedAt =
                resultSet.getTimestamp(
                        "last_ingested_at"
                );

        return new PolicyDocumentResponse(
                resultSet.getObject(
                        "policy_document_id",
                        UUID.class
                ),
                resultSet.getString(
                        "file_name"
                ),
                resultSet.getString(
                        "content_type"
                ),
                resultSet.getLong(
                        "file_size"
                ),
                resultSet.getString(
                        "status"
                ),
                resultSet.getInt(
                        "chunk_count"
                ),
                resultSet.getInt(
                        "embedding_count"
                ),
                resultSet.getTimestamp(
                        "uploaded_at"
                ).toInstant(),
                lastIngestedAt == null
                        ? null
                        : lastIngestedAt.toInstant(),
                resultSet.getString(
                        "error_message"
                )
        );
    }
}
