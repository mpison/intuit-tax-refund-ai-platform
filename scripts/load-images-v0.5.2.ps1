$ErrorActionPreference = "Stop"

kind load docker-image refund-status-service:0.5.2 `
  --name refund-demo

kind load docker-image customer-ui:0.5.2 `
  --name refund-demo
