$ErrorActionPreference = "Stop"

mvn `
  -f .\services\core\refund-status-service\pom.xml `
  clean package

docker build `
  --no-cache `
  -t refund-status-service:0.5.3 `
  .\services\core\refund-status-service
