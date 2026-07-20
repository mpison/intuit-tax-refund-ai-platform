# v0.5.7.2 — Spring AI Tool Foundation

## Added

- Internal `ToolRegistry` abstraction
- Spring AI 2.0-backed tool registry implementation
- Tool metadata model
- AI execution context
- Prompt context model and assembler
- RAG context provider contract
- Build, load, deploy, and verification scripts

## Scope

This revision introduces permanent internal abstractions only.

It does not:

- add REST diagnostics endpoints;
- invoke MCP tools from the chatbot;
- change current RAG behavior;
- change prompts used by the existing chat path.
