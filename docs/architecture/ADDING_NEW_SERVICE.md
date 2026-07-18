# Adding a New Service

Choose the category first.

Examples:

- New chatbot for support:
  services/ai/assistants/support-assistant-service

- New MCP server for operations:
  services/ai/mcp/operations-mcp-server

- New RAG retriever for tax policies:
  services/ai/rag/tax-policy-retrieval-service

- New model for fraud risk:
  services/ai/models/refund-fraud-model-service

- New external state tax adapter:
  services/integrations/state-tax-integration-service

Also add matching deployment configuration under:
- infrastructure/kubernetes/base/<category>
- infrastructure/helm/charts/<category>
