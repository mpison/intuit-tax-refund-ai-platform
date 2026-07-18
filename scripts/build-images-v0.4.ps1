$ErrorActionPreference = "Stop"

docker build `
  --no-cache `
  -t tax-policy-assistant-service:0.4.0 `
  .\services\ai\tax-policy-assistant-service

docker build `
  --no-cache `
  -t customer-ui:0.4.0 `
  .\applications\customer-ui
