from typing import Any

from mcp.server.fastmcp import FastMCP

from shared.app_factory import create_app
from shared.database import connection
from shared.graph import build_database_tool_graph


mcp = FastMCP(
    "refund-mcp-python",
    stateless_http=True,
    json_response=True,
)


def query_latest_refund(state: dict[str, Any]) -> dict[str, Any]:
    external_identity_id = state["input"]["external_identity_id"]

    with connection() as database_connection:
        with database_connection.cursor() as cursor:
            cursor.execute(
                '''
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
                WHERE u.external_identity_id = %s
                ORDER BY tr.filed_at DESC NULLS LAST
                LIMIT 1
                ''',
                (external_identity_id,),
            )
            row = cursor.fetchone()

    return {
        "result": (
            {"found": False, "externalIdentityId": external_identity_id}
            if row is None
            else {"found": True, **row}
        )
    }


latest_refund_graph = build_database_tool_graph(query_latest_refund)


@mcp.tool()
def get_latest_refund_by_identity(
    external_identity_id: str,
) -> dict[str, Any]:
    '''Get the most recent tax return and current refund status.'''
    return latest_refund_graph.invoke(
        {"input": {"external_identity_id": external_identity_id}}
    )["result"]


@mcp.tool()
def get_refund_history_by_identity(
    external_identity_id: str,
) -> list[dict[str, Any]]:
    '''Get refund history for the customer's most recent tax return.'''
    with connection() as database_connection:
        with database_connection.cursor() as cursor:
            cursor.execute(
                '''
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
                WHERE u.external_identity_id = %s
                  AND h.tax_return_id = (
                      SELECT tr2.tax_return_id
                      FROM tax_returns tr2
                      WHERE tr2.user_id = u.user_id
                      ORDER BY tr2.filed_at DESC NULLS LAST
                      LIMIT 1
                  )
                ORDER BY h.changed_at ASC
                ''',
                (external_identity_id,),
            )
            return list(cursor.fetchall())


app = create_app(mcp, "refund-mcp-python")
