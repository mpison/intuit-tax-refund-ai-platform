$ErrorActionPreference = "Stop"

docker build `
    --no-cache `
    -t refund-status-service:0.1.4 `
    .\services\core\refund-status-service

docker build `
    --no-cache `
    -t customer-ui:0.1.0 `
    .\applications\customer-ui
