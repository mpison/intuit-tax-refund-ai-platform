package com.refundplatform.admin.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final JdbcTemplate jdbcTemplate;

    public AdminController(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "totalUsers",
                queryRequiredCount(
                        "SELECT COUNT(*) FROM app_users"
                )
        );

        response.put(
                "totalTaxReturns",
                queryRequiredCount(
                        "SELECT COUNT(*) FROM tax_returns"
                )
        );

        response.put(
                "totalRefunds",
                queryRequiredCount(
                        "SELECT COUNT(*) FROM refund_statuses"
                )
        );

        Map<String, Long> refundsByStatus =
                new LinkedHashMap<>();

        jdbcTemplate.queryForList(
                """
                SELECT
                    current_status,
                    COUNT(*) AS status_count
                FROM refund_statuses
                GROUP BY current_status
                ORDER BY current_status
                """
        )
        .forEach(
                row -> {

                    Object statusObject =
                            row.get(
                                    "current_status"
                            );

                    Object countObject =
                            row.get(
                                    "status_count"
                            );

                    if (
                            statusObject != null
                            && countObject instanceof Number countNumber
                    ) {

                        refundsByStatus.put(
                                String.valueOf(
                                        statusObject
                                ),
                                countNumber.longValue()
                        );
                    }
                }
        );

        response.put(
                "refundsByStatus",
                refundsByStatus
        );

        return response;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> users(
            @RequestParam(
                    defaultValue = ""
            )
            String search) {

        String normalizedSearch =
                normalizeSearch(
                        search
                );

        return jdbcTemplate.queryForList(
                """
                SELECT
                    u.user_id,
                    u.external_identity_id,
                    u.display_name,
                    u.email,
                    u.first_name,
                    u.last_name,
                    u.created_at,
                    EXISTS (
                        SELECT 1
                        FROM tax_returns tr
                        WHERE tr.user_id = u.user_id
                    ) AS has_filed_return
                FROM app_users u
                WHERE
                    ? = ''
                    OR LOWER(
                        COALESCE(
                            u.display_name,
                            ''
                        )
                    ) LIKE LOWER(
                        '%' || ? || '%'
                    )
                    OR LOWER(
                        COALESCE(
                            u.email,
                            ''
                        )
                    ) LIKE LOWER(
                        '%' || ? || '%'
                    )
                ORDER BY
                    u.created_at DESC NULLS LAST
                LIMIT 100
                """,
                normalizedSearch,
                normalizedSearch,
                normalizedSearch
        );
    }

    @GetMapping("/refunds")
    public List<Map<String, Object>> refunds(
            @RequestParam(
                    defaultValue = ""
            )
            String search) {

        String normalizedSearch =
                normalizeSearch(
                        search
                );

        return jdbcTemplate.queryForList(
                """
                SELECT
                    tr.tax_return_id,
                    tr.user_id,
                    u.display_name AS customer_name,
                    u.email,
                    tr.tax_year,
                    tr.refund_amount,
                    tr.external_refund_id,
                    rs.current_status AS status,
                    tr.filed_at,
                    rs.last_checked_at
                FROM tax_returns tr
                JOIN app_users u
                    ON u.user_id = tr.user_id
                JOIN refund_statuses rs
                    ON rs.tax_return_id =
                        tr.tax_return_id
                WHERE
                    ? = ''
                    OR LOWER(
                        COALESCE(
                            u.display_name,
                            ''
                        )
                    ) LIKE LOWER(
                        '%' || ? || '%'
                    )
                    OR LOWER(
                        COALESCE(
                            u.email,
                            ''
                        )
                    ) LIKE LOWER(
                        '%' || ? || '%'
                    )
                    OR LOWER(
                        COALESCE(
                            tr.external_refund_id,
                            ''
                        )
                    ) LIKE LOWER(
                        '%' || ? || '%'
                    )
                ORDER BY
                    rs.last_checked_at DESC NULLS LAST
                LIMIT 100
                """,
                normalizedSearch,
                normalizedSearch,
                normalizedSearch,
                normalizedSearch
        );
    }

    private long queryRequiredCount(
            String sql) {

        Long value =
                jdbcTemplate.queryForObject(
                        sql,
                        Long.class
                );

        return value == null
                ? 0L
                : value;
    }

    private String normalizeSearch(
            String search) {

        return search == null
                ? ""
                : search.trim();
    }
}
