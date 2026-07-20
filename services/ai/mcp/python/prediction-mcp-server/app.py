from datetime import date, datetime, timedelta
from typing import Any
from uuid import UUID

from mcp.server.fastmcp import FastMCP

from shared.app_factory import create_app
from shared.database import connection
from shared.graph import build_database_tool_graph


mcp = FastMCP(
    "prediction-mcp-python",
    stateless_http=True,
    json_response=True,
)


def as_date(value: Any) -> date | None:
    if value is None:
        return None
    if isinstance(value, datetime):
        return value.date()
    if isinstance(value, date):
        return value
    return date.fromisoformat(str(value)[:10])


def calculate_prediction(state: dict[str, Any]) -> dict[str, Any]:
    tax_return_id = state["input"]["tax_return_id"]
    processing_days = state["input"]["policy_processing_days"]

    with connection() as database_connection:
        with database_connection.cursor() as cursor:
            cursor.execute(
                '''
                SELECT
                    tr.tax_return_id,
                    tr.filed_at,
                    rs.current_status,
                    rs.official_refund_date
                FROM tax_returns tr
                LEFT JOIN refund_statuses rs
                  ON rs.tax_return_id = tr.tax_return_id
                WHERE tr.tax_return_id = %s
                LIMIT 1
                ''',
                (UUID(tax_return_id),),
            )
            row = cursor.fetchone()

    if row is None:
        return {"result": {"found": False, "taxReturnId": tax_return_id}}

    filed_date = as_date(row["filed_at"])
    official_date = as_date(row["official_refund_date"])

    estimated_date = (
        official_date
        if official_date is not None
        else filed_date + timedelta(days=processing_days)
        if filed_date is not None
        else None
    )

    days_remaining = (
        max(0, (estimated_date - date.today()).days)
        if estimated_date is not None
        else None
    )

    return {
        "result": {
            "found": True,
            "taxReturnId": tax_return_id,
            "estimatedRefundDate": (
                estimated_date.isoformat()
                if estimated_date is not None
                else None
            ),
            "estimatedDaysRemaining": days_remaining,
            "confidence": 1.0 if official_date is not None else 0.65,
            "method": (
                "OFFICIAL_IRS_DATE"
                if official_date is not None
                else "POLICY_CALCULATION"
            ),
            "explanation": (
                "Used the official refund date."
                if official_date is not None
                else f"Calculated filed date plus {processing_days} policy calendar days."
            ),
        }
    }


prediction_graph = build_database_tool_graph(calculate_prediction)


@mcp.tool()
def predict_refund_date(
    tax_return_id: str,
    policy_processing_days: int = 21,
) -> dict[str, Any]:
    '''Calculate an estimated refund date from official or policy data.'''
    return prediction_graph.invoke(
        {
            "input": {
                "tax_return_id": tax_return_id,
                "policy_processing_days": policy_processing_days,
            }
        }
    )["result"]


app = create_app(mcp, "prediction-mcp-python")
