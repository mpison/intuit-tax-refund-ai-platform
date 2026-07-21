$ErrorActionPreference = "Stop"

mvn `
  -f .\services\ai\tax-policy-assistant-service\pom.xml `
  clean package

docker build `
  --no-cache `
  -t tax-policy-assistant-service:0.5.7.4 `
  .\services\ai\tax-policy-assistant-service
