# v0.5.7.0 — Switchable MCP Foundation

## Added

- Java Spring AI MCP implementations
- Python FastAPI/FastMCP implementations
- LangGraph-backed Python tool execution
- Matching contracts across both runtimes
- Stable Kubernetes MCP service names
- Runtime switching by service selector
- Customer, refund, and prediction MCP domains

## Switch examples

```powershell
.\scripts\switch-mcp-runtime.ps1 -Runtime java
.\scripts\switch-mcp-runtime.ps1 -Runtime python
.\scripts\switch-mcp-runtime.ps1 -Runtime python -Domain prediction
```
