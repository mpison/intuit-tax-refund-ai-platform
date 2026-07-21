# v0.5.7.4 — Unified MCP and Policy Tool Agent

## Added

- All discovered MCP tools registered as default ChatClient callbacks
- PGVector exposed as the local `search_tax_policy` tool
- One Spring AI tool-calling loop
- Model-directed multi-tool execution
- Policy source metadata returned by the vector-search tool
- Tool-calling debug logging
- Version-independent Docker JAR copy

## Runtime flow

```text
Question
  -> ChatClient
  -> model selects tools
       -> Customer MCP
       -> Refund MCP
       -> Refund History MCP
       -> Prediction MCP
       -> search_tax_policy (PGVector)
  -> Spring AI ToolCallingAdvisor manages the loop
  -> one final grounded answer
```

## Known limitation

Authenticated customer identity has not yet been propagated into tool context.
Personalized MCP tools therefore require a valid identity from trusted context
before they can be invoked safely.
