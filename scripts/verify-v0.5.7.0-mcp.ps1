$ErrorActionPreference = "Stop"

kubectl get pods `
  -n refund-platform `
  -l "app in (customer-mcp,refund-mcp,prediction-mcp)"

kubectl get svc `
  -n refund-platform `
  customer-mcp `
  refund-mcp `
  prediction-mcp

Write-Host ""
Write-Host "Stable MCP endpoints:"
Write-Host "customer:   http://customer-mcp:8030/mcp"
Write-Host "refund:     http://refund-mcp:8031/mcp"
Write-Host "prediction: http://prediction-mcp:8032/mcp"
