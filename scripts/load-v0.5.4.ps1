$ErrorActionPreference = "Stop"

kind load docker-image irs-simulator:0.5.4 `
  --name refund-demo

kind load docker-image irs-admin-ui:0.5.4 `
  --name refund-demo
