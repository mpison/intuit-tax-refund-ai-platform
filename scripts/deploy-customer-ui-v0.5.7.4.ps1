$ErrorActionPreference = "Stop"

kubectl set image `
  deployment/customer-ui `
  customer-ui=customer-ui:0.5.7.4 `
  -n refund-platform

kubectl rollout status `
  deployment/customer-ui `
  -n refund-platform `
  --timeout=300s
