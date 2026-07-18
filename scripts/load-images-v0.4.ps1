$ErrorActionPreference = "Stop"

kind load docker-image tax-policy-assistant-service:0.4.0 `
  --name refund-demo

kind load docker-image customer-ui:0.4.0 `
  --name refund-demo
