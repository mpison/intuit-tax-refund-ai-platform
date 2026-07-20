package com.refundplatform.mcp.tool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RefundTools {

    private final JdbcTemplate jdbcTemplate;

    public RefundTools(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @McpTool(
            name = "get_latest_refund_by_identity",
            description = "Get the most recently filed tax return and current refund status for a customer identity.",
            generateOutputSchema = true
    )
    public Map<String, Object> getLatestRefundByIdentity(
            @McpToolParam(
                    description = "Keycloak subject stored in app_users.external_identity_id",
                    required = true
            )
            String externalIdentityId) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                """
                SELECT
                    tr.tax_return_id,
                    tr.user_id,
                    tr.tax_year,
                    tr.filed_at,
                    tr.refund_amount,
                    tr.external_refund_id,
                    rs.current_status,
                    rs.official_refund_date,
                    rs.last_checked_at
                FROM app_users u
                JOIN tax_returns tr
                  ON tr.user_id = u.user_id
                LEFT JOIN refund_statuses rs
                  ON rs.tax_return_id = tr.tax_return_id
                WHERE u.external_identity_id = ?
                ORDER BY tr.filed_at DESC NULLS LAST
                LIMIT 1
                """,
                externalIdentityId
        );

        if (rows.isEmpty()) {
            return Map.of(
                    "found", false,
                    "externalIdentityId", externalIdentityId
            );
        }

        return rows.getFirst();
    }

    @McpTool(
            name = "get_refund_history_by_identity",
            description = "Get refund status history for the customer's most recent tax return.",
            generateOutputSchema = true
    )
    public Map<String, Object> getRefundHistoryByIdentity(
            @McpToolParam(
                    description = "Keycloak subject stored in app_users.external_identity_id",
                    required = true
            )
            String externalIdentityId) {

        List<Map<String, Object>> historyList =
                jdbcTemplate.queryForList(
                        """
                        SELECT
                            h.tax_return_id,
                            h.status,
                            h.source,
                            h.changed_at
                        FROM refund_status_history h
                        JOIN tax_returns tr
                          ON tr.tax_return_id = h.tax_return_id
                        JOIN app_users u
                          ON u.user_id = tr.user_id
                        WHERE u.external_identity_id = ?
                          AND h.tax_return_id = (
                              SELECT tr2.tax_return_id
                              FROM tax_returns tr2
                              WHERE tr2.user_id = u.user_id
                              ORDER BY tr2.filed_at DESC NULLS LAST
                              LIMIT 1
                          )
                        ORDER BY h.changed_at ASC
                        """,
                        externalIdentityId
                );

        Map<String, Object> responseMap =
                new LinkedHashMap<>();

        responseMap.put(
                "externalIdentityId",
                externalIdentityId
        );

        responseMap.put(
                "historyCount",
                historyList.size()
        );

        responseMap.put(
                "history",
                historyList
        );

        return responseMap;
    }
}
