$ErrorActionPreference = "Stop"

kind load docker-image `
  customer-ui:0.5.7.4 `
  --name refund-demo
