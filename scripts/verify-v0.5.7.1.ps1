$ErrorActionPreference = "Stop"

kubectl get svc `
  -n refund-platform `
  customer-mcp `
  refund-mcp `
  prediction-mcp

kubectl get deployment tax-policy-assistant-service `
  -n refund-platform `
  -o custom-columns="NAME:.metadata.name,READY:.status.readyReplicas,IMAGE:.spec.template.spec.containers[0].image"

Write-Host ""
Write-Host "Port-forward:"
Write-Host "kubectl port-forward -n refund-platform svc/tax-policy-assistant-service 8060:8060"
Write-Host "Test:"
Write-Host "Invoke-RestMethod http://localhost:8060/api/v1/ai/mcp/tools"
