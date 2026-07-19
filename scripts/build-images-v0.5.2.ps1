$ErrorActionPreference = "Stop"

mvn `
  -f .\services\core\refund-status-service\pom.xml `
  clean package

docker build `
  --no-cache `
  -t refund-status-service:0.5.2 `
  .\services\core\refund-status-service

docker build `
  --no-cache `
  -t customer-ui:0.5.2 `
  .\applications\customer-ui
