$ErrorActionPreference = "Stop"

kubectl delete job refund-history-schema-v053 `
  -n refund-platform `
  --ignore-not-found

kubectl apply `
  -f .\infrastructure\kubernetes\jobs\refund-history-schema-job.yaml

kubectl wait `
  --for=condition=complete `
  job/refund-history-schema-v053 `
  -n refund-platform `
  --timeout=180s

kubectl logs `
  job/refund-history-schema-v053 `
  -n refund-platform
