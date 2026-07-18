$ErrorActionPreference = "Stop"

docker build --no-cache `
  -t refund-status-service:0.3.0 `
  .\services\core\refund-status-service

docker build --no-cache `
  -t customer-ui:0.3.0 `
  .\applications\customer-ui

docker build --no-cache `
  -t refund-prediction-service:0.3.0 `
  .\services\ai\refund-prediction-service
