from typing import Any

from mcp.server.fastmcp import FastMCP

from shared.app_factory import create_app
from shared.database import connection
from shared.graph import build_database_tool_graph


mcp = FastMCP(
    "customer-mcp-python",
    stateless_http=True,
    json_response=True,
)


def query_customer(state: dict[str, Any]) -> dict[str, Any]:
    external_identity_id = state["input"]["external_identity_id"]

    with connection() as database_connection:
        with database_connection.cursor() as cursor:
            cursor.execute(
                '''
                SELECT
                    user_id,
                    external_identity_id,
                    display_name,
                    email,
                    first_name,
                    last_name,
                    created_at
                FROM app_users
                WHERE external_identity_id = %s
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


customer_graph = build_database_tool_graph(query_customer)


@mcp.tool()
def get_customer_by_identity(
    external_identity_id: str,
) -> dict[str, Any]:
    '''Get a customer profile using the Keycloak external identity subject.'''
    return customer_graph.invoke(
        {"input": {"external_identity_id": external_identity_id}}
    )["result"]


app = create_app(mcp, "customer-mcp-python")
