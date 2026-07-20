package com.refundplatform.mcp.tool;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerTools {

    private final JdbcTemplate jdbcTemplate;

    public CustomerTools(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @McpTool(
            name = "get_customer_by_identity",
            description = "Get a customer profile using the Keycloak external identity subject.",
            generateOutputSchema = true
    )
    public Map<String, Object> getCustomerByIdentity(
            @McpToolParam(
                    description = "Keycloak subject stored in app_users.external_identity_id",
                    required = true
            )
            String externalIdentityId) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                """
                SELECT
                    user_id,
                    external_identity_id,
                    display_name,
                    email,
                    first_name,
                    last_name,
                    created_at
                FROM app_users
                WHERE external_identity_id = ?
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

        Map<String, Object> row = rows.getFirst();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("found", true);
        response.put("userId", row.get("user_id"));
        response.put("externalIdentityId", row.get("external_identity_id"));
        response.put("displayName", row.get("display_name"));
        response.put("email", row.get("email"));
        response.put("firstName", row.get("first_name"));
        response.put("lastName", row.get("last_name"));
        response.put("createdAt", row.get("created_at"));
        return response;
    }
}
