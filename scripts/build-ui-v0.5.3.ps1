$ErrorActionPreference = "Stop"

docker build `
  --no-cache `
  -t customer-ui:0.5.3 `
  .\applications\customer-ui
