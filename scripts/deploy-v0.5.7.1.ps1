$ErrorActionPreference = "Stop"

kubectl set image `
  deployment/tax-policy-assistant-service `
  tax-policy-assistant-service=tax-policy-assistant-service:0.5.7.1 `
  -n refund-platform

kubectl set env `
  deployment/tax-policy-assistant-service `
  CUSTOMER_MCP_URL=http://customer-mcp:8030 `
  REFUND_MCP_URL=http://refund-mcp:8031 `
  PREDICTION_MCP_URL=http://prediction-mcp:8032 `
  -n refund-platform

kubectl rollout status `
  deployment/tax-policy-assistant-service `
  -n refund-platform `
  --timeout=300s
