$ErrorActionPreference = "Stop"

kind load docker-image `
  tax-policy-assistant-service:0.5.7.4 `
  --name refund-demo
