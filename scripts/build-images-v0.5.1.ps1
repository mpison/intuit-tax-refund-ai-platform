$ErrorActionPreference = "Stop"

docker build `
  --no-cache `
  -t refund-status-service:0.5.1 `
  .\services\core\refund-status-service

docker build `
  --no-cache `
  -t customer-ui:0.5.1 `
  .\applications\customer-ui
