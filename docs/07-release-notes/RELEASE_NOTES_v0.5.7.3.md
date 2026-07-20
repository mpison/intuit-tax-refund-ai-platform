# v0.5.7.3 — AI Intent Routing and Orchestration Planning

## Added

- Deterministic question-intent classification
- Policy-only, account-only, and mixed intent categories
- MCP tool-selection planning
- Required-tool availability validation
- Internal AI orchestration plan
- Unit tests for intent classification

## Behavior

This revision does not yet execute MCP tools from chat.

It prepares an execution plan:

```text
Policy question
  -> RAG only

Account question
  -> Customer MCP
  -> Latest Refund MCP
  -> optional Prediction MCP

Mixed question
  -> MCP context
  -> policy RAG
```

The existing chatbot path remains unchanged until the orchestration plan is
wired into the chat service in the next revision.
