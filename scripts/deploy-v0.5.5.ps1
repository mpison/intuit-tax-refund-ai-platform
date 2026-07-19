$ErrorActionPreference = "Stop"

kubectl apply -f .\infrastructure\kubernetes\base\admin-service.yaml
kubectl apply -f .\infrastructure\kubernetes\base\admin-ui.yaml

kubectl rollout status deployment/admin-service -n refund-platform --timeout=180s
kubectl rollout status deployment/admin-ui -n refund-platform --timeout=120s
