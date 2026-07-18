$ErrorActionPreference = "Stop"

kind load docker-image refund-status-service:0.2.0 `
    --name refund-demo

kind load docker-image customer-ui:0.2.0 `
    --name refund-demo

kind load docker-image irs-simulator:0.2.0 `
    --name refund-demo

Write-Host "All v0.2.0 images loaded into refund-demo."
