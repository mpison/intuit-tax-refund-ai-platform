$ErrorActionPreference = "Stop"

kind load docker-image `
  tax-policy-assistant-service:0.5.7.2 `
  --name refund-demo
