$ErrorActionPreference = "Stop"

docker build `
  --no-cache `
  -t customer-ui:0.4.3 `
  .\applications\customer-ui

kind load docker-image customer-ui:0.4.3 `
  --name refund-demo

kubectl apply -k infrastructure\kubernetes\overlays\local

kubectl rollout status deployment/customer-ui `
  -n refund-platform `
  --timeout=180s
