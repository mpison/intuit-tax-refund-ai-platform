package com.refundplatform.mcp.tool;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PredictionTools {

    private static final int DEFAULT_PROCESSING_DAYS =
            21;

    private static final int MIN_PROCESSING_DAYS =
            1;

    private static final int MAX_PROCESSING_DAYS =
            365;

    private final JdbcTemplate jdbcTemplate;

    public PredictionTools(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }

    @McpTool(
            name = "predict_refund_date",
            description = """
                    Calculate a deterministic expected refund date from
                    the tax return filing date and the supplied standard
                    policy-processing duration.

                    The returned date is a policy-based estimate and is
                    not an official IRS refund date.
                    """,
            generateOutputSchema = true
    )
    public Map<String, Object> predictRefundDate(
            @McpToolParam(
                    description = "Tax return UUID",
                    required = true
            )
            String taxReturnId,

            @McpToolParam(
                    description = """
                            Standard policy-processing duration in
                            calendar days. Defaults to 21.
                            """,
                    required = false
            )
            Integer policyProcessingDays) {

        UUID taxReturnUuid =
                parseTaxReturnId(
                        taxReturnId
                );

        if (taxReturnUuid == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    false,

                    "taxReturnId",
                    safeValue(
                            taxReturnId
                    ),

                    "error",
                    "taxReturnId must be a valid UUID."
            );
        }

        Integer processingDays =
                normalizeProcessingDays(
                        policyProcessingDays
                );

        if (processingDays == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    false,

                    "taxReturnId",
                    taxReturnId,

                    "error",
                    "policyProcessingDays must be between "
                            + MIN_PROCESSING_DAYS
                            + " and "
                            + MAX_PROCESSING_DAYS
                            + "."
            );
        }

        Map<String, Object> taxReturn =
                findTaxReturn(
                        taxReturnUuid
                );

        if (taxReturn == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "taxReturnId",
                    taxReturnId,

                    "error",
                    "No matching tax return was found."
            );
        }

        Map<String, Object> refundStatus =
                findRefundStatus(
                        taxReturnUuid
                );

        LocalDate filedDate =
                toLocalDate(
                        taxReturn.get(
                                "filed_at"
                        )
                );

        LocalDate estimatedRefundDate =
                filedDate == null
                        ? null
                        : filedDate.plusDays(
                                processingDays
                        );

        LocalDate currentDate =
                LocalDate.now();

        Long estimatedDaysRemaining =
                estimatedRefundDate == null
                        ? null
                        : Math.max(
                                0,
                                ChronoUnit.DAYS.between(
                                        currentDate,
                                        estimatedRefundDate
                                )
                        );

        boolean estimatedDatePassed =
                estimatedRefundDate != null
                        && estimatedRefundDate.isBefore(
                                currentDate
                        );

        String currentStatus =
                refundStatus == null
                        ? null
                        : stringValue(
                                refundStatus.get(
                                        "current_status"
                                )
                        );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "found",
                true
        );

        response.put(
                "validInput",
                true
        );

        response.put(
                "taxReturnId",
                taxReturnId
        );

        response.put(
                "taxYear",
                taxReturn.get(
                        "tax_year"
                )
        );

        response.put(
                "filedDate",
                filedDate
        );

        response.put(
                "refundAmount",
                taxReturn.get(
                        "refund_amount"
                )
        );

        response.put(
                "filingMethod",
                taxReturn.get(
                        "filing_method"
                )
        );

        response.put(
                "currentStatus",
                currentStatus
        );

        response.put(
                "policyProcessingDays",
                processingDays
        );

        response.put(
                "estimatedRefundDate",
                estimatedRefundDate
        );

        response.put(
                "estimatedDaysRemaining",
                estimatedDaysRemaining
        );

        response.put(
                "estimatedDatePassed",
                estimatedDatePassed
        );

        response.put(
                "isOfficialDate",
                false
        );

        response.put(
                "method",
                "POLICY_CALCULATION"
        );

        response.put(
                "confidence",
                calculateConfidence(
                        currentStatus,
                        estimatedDatePassed
                )
        );

        response.put(
                "lastCheckedAt",
                refundStatus == null
                        ? null
                        : refundStatus.get(
                                "last_checked_at"
                        )
        );

        response.put(
                "lastExternalSyncAt",
                refundStatus == null
                        ? null
                        : refundStatus.get(
                                "last_external_sync_at"
                        )
        );

        response.put(
                "externalSource",
                refundStatus == null
                        ? null
                        : refundStatus.get(
                                "external_source"
                        )
        );

        response.put(
                "explanation",
                buildExplanation(
                        filedDate,
                        processingDays,
                        currentStatus,
                        estimatedDatePassed
                )
        );

        return response;
    }

    private Map<String, Object> findTaxReturn(
            UUID taxReturnId) {

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                        """
                        SELECT *
                        FROM tax_returns
                        WHERE tax_return_id = ?
                        LIMIT 1
                        """,
                        taxReturnId
                );

        return rows.isEmpty()
                ? null
                : rows.getFirst();
    }

    private Map<String, Object> findRefundStatus(
            UUID taxReturnId) {

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                        """
                        SELECT *
                        FROM refund_statuses
                        WHERE tax_return_id = ?
                        LIMIT 1
                        """,
                        taxReturnId
                );

        return rows.isEmpty()
                ? null
                : rows.getFirst();
    }

    private UUID parseTaxReturnId(
            String taxReturnId) {

        if (
                taxReturnId == null
                || taxReturnId.isBlank()
        ) {
            return null;
        }

        try {
            return UUID.fromString(
                    taxReturnId.trim()
            );
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private Integer normalizeProcessingDays(
            Integer policyProcessingDays) {

        if (policyProcessingDays == null) {
            return DEFAULT_PROCESSING_DAYS;
        }

        if (
                policyProcessingDays < MIN_PROCESSING_DAYS
                || policyProcessingDays > MAX_PROCESSING_DAYS
        ) {
            return null;
        }

        return policyProcessingDays;
    }

    private double calculateConfidence(
            String currentStatus,
            boolean estimatedDatePassed) {

        if (estimatedDatePassed) {
            return 0.40;
        }

        if (currentStatus == null) {
            return 0.50;
        }

        return switch (
                currentStatus.toUpperCase()
        ) {

            case "APPROVED" ->
                    0.85;

            case "PROCESSING" ->
                    0.70;

            case "FILED" ->
                    0.60;

            case "REJECTED" ->
                    0.20;

            default ->
                    0.55;
        };
    }

    private String buildExplanation(
            LocalDate filedDate,
            int processingDays,
            String currentStatus,
            boolean estimatedDatePassed) {

        if (filedDate == null) {

            return "An expected refund date could not be calculated "
                    + "because the filing date is unavailable.";
        }

        String explanation =
                "Calculated the expected refund date as filing date "
                        + filedDate
                        + " plus "
                        + processingDays
                        + " calendar days.";

        if (currentStatus != null) {

            explanation +=
                    " The latest refund status is "
                            + currentStatus
                            + ".";
        }

        if (estimatedDatePassed) {

            explanation +=
                    " The estimated date has already passed, so the "
                            + "return may require additional review or "
                            + "updated IRS status information.";
        }

        return explanation;
    }

    private LocalDate toLocalDate(
            Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof LocalDate localDate) {
            return localDate;
        }

        if (value instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toLocalDate();
        }

        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime.toLocalDate();
        }

        if (value instanceof Timestamp timestamp) {
            return timestamp
                    .toLocalDateTime()
                    .toLocalDate();
        }

        if (value instanceof Date date) {
            return date.toLocalDate();
        }

        String stringValue =
                String.valueOf(
                        value
                );

        if (stringValue.length() < 10) {
            return null;
        }

        try {
            return LocalDate.parse(
                    stringValue.substring(
                            0,
                            10
                    )
            );
        }
        catch (RuntimeException exception) {
            return null;
        }
    }

    private String stringValue(
            Object value) {

        return value == null
                ? null
                : String.valueOf(
                        value
                );
    }

    private String safeValue(
            String value) {

        return value == null
                ? ""
                : value;
    }
}