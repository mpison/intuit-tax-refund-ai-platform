$ErrorActionPreference = "Stop"

kubectl get deployment tax-policy-assistant-service `
  -n refund-platform `
  -o custom-columns="NAME:.metadata.name,READY:.status.readyReplicas,IMAGE:.spec.template.spec.containers[0].image"

kubectl logs deployment/tax-policy-assistant-service `
  -n refund-platform `
  --tail=250
