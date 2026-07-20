$ErrorActionPreference = "Stop"

kubectl set image `
  deployment/tax-policy-assistant-service `
  tax-policy-assistant-service=tax-policy-assistant-service:0.5.7.3 `
  -n refund-platform

kubectl rollout status `
  deployment/tax-policy-assistant-service `
  -n refund-platform `
  --timeout=300s
