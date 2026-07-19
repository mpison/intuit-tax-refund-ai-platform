$ErrorActionPreference = "Stop"

kubectl delete job irs-simulator-schema-v054 `
  -n refund-platform `
  --ignore-not-found

kubectl delete configmap irs-simulator-schema-v054 `
  -n refund-platform `
  --ignore-not-found

kubectl apply `
  -f .\infrastructure\kubernetes\jobs\irs-simulator-schema-v054.yaml

kubectl wait `
  --for=condition=complete `
  job/irs-simulator-schema-v054 `
  -n refund-platform `
  --timeout=180s

kubectl apply `
  -f .\infrastructure\kubernetes\base\irs-simulator-v0.5.4.yaml

kubectl apply `
  -f .\infrastructure\kubernetes\base\irs-admin-ui.yaml

kubectl rollout status deployment/irs-simulator `
  -n refund-platform `
  --timeout=180s

kubectl rollout status deployment/irs-admin-ui `
  -n refund-platform `
  --timeout=120s
