$ErrorActionPreference = "Stop"

Write-Host "Building refund-status-service:0.2.0"

docker build `
    --no-cache `
    -t refund-status-service:0.2.0 `
    .\services\core\refund-status-service

Write-Host "Building customer-ui:0.2.0"

docker build `
    --no-cache `
    -t customer-ui:0.2.0 `
    .\applications\customer-ui

Write-Host "Building irs-simulator:0.2.0"

docker build `
    --no-cache `
    -t irs-simulator:0.2.0 `
    .\services\integrations\irs-simulator

Write-Host "All v0.2.0 images built successfully."
