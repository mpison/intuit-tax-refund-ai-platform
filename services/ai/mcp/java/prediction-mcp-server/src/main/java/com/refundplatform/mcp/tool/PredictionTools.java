package com.refundplatform.mcp.tool;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class PredictionTools {

    private final JdbcTemplate jdbcTemplate;

    public PredictionTools(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @McpTool(
            name = "predict_refund_date",
            description = "Calculate a deterministic refund-date estimate from official date or policy processing days.",
            generateOutputSchema = true
    )
    public Map<String, Object> predictRefundDate(
            @McpToolParam(description = "Tax return identifier", required = true)
            String taxReturnId,

            @McpToolParam(
                    description = "Standard policy processing duration in calendar days",
                    required = false
            )
            Integer policyProcessingDays) {

        int processingDays = policyProcessingDays == null
                ? 21
                : policyProcessingDays;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                """
                SELECT
                    tr.tax_return_id,
                    tr.filed_at,
                    rs.current_status,
                    rs.official_refund_date
                FROM tax_returns tr
                LEFT JOIN refund_statuses rs
                  ON rs.tax_return_id = tr.tax_return_id
                WHERE tr.tax_return_id = ?
                LIMIT 1
                """,
                UUID.fromString(taxReturnId)
        );

        if (rows.isEmpty()) {
            return Map.of(
                    "found", false,
                    "taxReturnId", taxReturnId
            );
        }

        Map<String, Object> row = rows.getFirst();
        LocalDate filedDate = toLocalDate(row.get("filed_at"));
        LocalDate officialDate = toLocalDate(row.get("official_refund_date"));

        LocalDate estimatedDate = officialDate != null
                ? officialDate
                : filedDate == null
                        ? null
                        : filedDate.plusDays(processingDays);

        Long daysRemaining = estimatedDate == null
                ? null
                : Math.max(
                        0,
                        ChronoUnit.DAYS.between(
                                LocalDate.now(),
                                estimatedDate
                        )
                );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("found", true);
        response.put("taxReturnId", taxReturnId);
        response.put("estimatedRefundDate", estimatedDate);
        response.put("estimatedDaysRemaining", daysRemaining);
        response.put("confidence", officialDate != null ? 1.0 : 0.65);
        response.put(
                "method",
                officialDate != null
                        ? "OFFICIAL_IRS_DATE"
                        : "POLICY_CALCULATION"
        );
        response.put(
                "explanation",
                officialDate != null
                        ? "Used the official refund date."
                        : "Calculated filed date plus "
                                + processingDays
                                + " policy calendar days."
        );
        return response;
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        if (value instanceof java.sql.Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        return LocalDate.parse(String.valueOf(value).substring(0, 10));
    }
}
