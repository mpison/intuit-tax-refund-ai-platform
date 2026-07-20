from typing import Any, TypedDict

from langgraph.graph import END, START, StateGraph


class ToolState(TypedDict, total=False):
    input: dict[str, Any]
    result: Any


def build_database_tool_graph(query_node):
    graph = StateGraph(ToolState)
    graph.add_node("query_database", query_node)
    graph.add_edge(START, "query_database")
    graph.add_edge("query_database", END)
    return graph.compile()
