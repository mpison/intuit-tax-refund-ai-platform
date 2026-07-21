package com.refundplatform.mcp.tool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RefundTools {

    private final JdbcTemplate jdbcTemplate;

    public RefundTools(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }

    @McpTool(
            name = "get_latest_refund_by_identity",
            description = """
                    Get the customer's most recently filed tax return
                    together with its current refund status.

                    The external identity must be the Keycloak subject
                    stored in app_users.external_identity_id.
                    """,
            generateOutputSchema = true
    )
    public Map<String, Object> getLatestRefundByIdentity(
            @McpToolParam(
                    description = """
                            Keycloak subject stored in
                            app_users.external_identity_id
                            """,
                    required = true
            )
            String externalIdentityId) {

        if (
                externalIdentityId == null
                || externalIdentityId.isBlank()
        ) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    false,

                    "error",
                    "externalIdentityId is required."
            );
        }

        Map<String, Object> user =
                findUserByExternalIdentity(
                        externalIdentityId
                );

        if (user == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "externalIdentityId",
                    externalIdentityId,

                    "error",
                    "No matching customer was found."
            );
        }

        UUID userId =
                toUuid(
                        user.get(
                                "user_id"
                        )
                );

        if (userId == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "externalIdentityId",
                    externalIdentityId,

                    "error",
                    "The matching customer has no valid user ID."
            );
        }

        Map<String, Object> taxReturn =
                findLatestTaxReturn(
                        userId
                );

        if (taxReturn == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "externalIdentityId",
                    externalIdentityId,

                    "userId",
                    userId,

                    "error",
                    "No tax return was found for the customer."
            );
        }

        UUID taxReturnId =
                toUuid(
                        taxReturn.get(
                                "tax_return_id"
                        )
                );

        Map<String, Object> refundStatus =
                taxReturnId == null
                        ? null
                        : findRefundStatus(
                                taxReturnId
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
                "externalIdentityId",
                externalIdentityId
        );

        response.put(
                "customer",
                user
        );

        response.put(
                "taxReturn",
                taxReturn
        );

        response.put(
                "refundStatus",
                refundStatus == null
                        ? Map.of(
                                "found",
                                false
                        )
                        : refundStatus
        );

        return response;
    }

    @McpTool(
            name = "get_refund_history_by_identity",
            description = """
                    Get the refund status-change history for the
                    customer's most recently filed tax return.

                    The external identity must be the Keycloak subject
                    stored in app_users.external_identity_id.
                    """,
            generateOutputSchema = true
    )
    public Map<String, Object> getRefundHistoryByIdentity(
            @McpToolParam(
                    description = """
                            Keycloak subject stored in
                            app_users.external_identity_id
                            """,
                    required = true
            )
            String externalIdentityId) {

        if (
                externalIdentityId == null
                || externalIdentityId.isBlank()
        ) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    false,

                    "error",
                    "externalIdentityId is required."
            );
        }

        Map<String, Object> user =
                findUserByExternalIdentity(
                        externalIdentityId
                );

        if (user == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "externalIdentityId",
                    externalIdentityId,

                    "historyCount",
                    0,

                    "history",
                    List.of(),

                    "error",
                    "No matching customer was found."
            );
        }

        UUID userId =
                toUuid(
                        user.get(
                                "user_id"
                        )
                );

        Map<String, Object> taxReturn =
                userId == null
                        ? null
                        : findLatestTaxReturn(
                                userId
                        );

        if (taxReturn == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "externalIdentityId",
                    externalIdentityId,

                    "historyCount",
                    0,

                    "history",
                    List.of(),

                    "error",
                    "No tax return was found for the customer."
            );
        }

        UUID taxReturnId =
                toUuid(
                        taxReturn.get(
                                "tax_return_id"
                        )
                );

        if (taxReturnId == null) {

            return Map.of(
                    "found",
                    false,

                    "validInput",
                    true,

                    "externalIdentityId",
                    externalIdentityId,

                    "historyCount",
                    0,

                    "history",
                    List.of(),

                    "error",
                    "The latest tax return has no valid ID."
            );
        }

        List<Map<String, Object>> history =
                jdbcTemplate.queryForList(
                        """
                        SELECT
                            refund_status_history_id,
                            tax_return_id,
                            previous_status,
                            new_status,
                            source,
                            changed_at
                        FROM refund_status_history
                        WHERE tax_return_id = ?
                        ORDER BY changed_at ASC
                        """,
                        taxReturnId
                );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "found",
                !history.isEmpty()
        );

        response.put(
                "validInput",
                true
        );

        response.put(
                "externalIdentityId",
                externalIdentityId
        );

        response.put(
                "taxReturnId",
                taxReturnId
        );

        response.put(
                "historyCount",
                history.size()
        );

        response.put(
                "history",
                history
        );

        return response;
    }

    private Map<String, Object> findUserByExternalIdentity(
            String externalIdentityId) {

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                        """
                        SELECT *
                        FROM app_users
                        WHERE external_identity_id = ?
                        LIMIT 1
                        """,
                        externalIdentityId
                );

        return rows.isEmpty()
                ? null
                : rows.getFirst();
    }

    private Map<String, Object> findLatestTaxReturn(
            UUID userId) {

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                        """
                        SELECT *
                        FROM tax_returns
                        WHERE user_id = ?
                        ORDER BY filed_at DESC
                        LIMIT 1
                        """,
                        userId
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

    private UUID toUuid(
            Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof UUID uuid) {
            return uuid;
        }

        try {
            return UUID.fromString(
                    String.valueOf(
                            value
                    )
            );
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }
}