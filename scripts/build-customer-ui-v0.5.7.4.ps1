$ErrorActionPreference = "Stop"

docker build `
  --no-cache `
  -t customer-ui:0.5.7.4 `
  .\applications\customer-ui
