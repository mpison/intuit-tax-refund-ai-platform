$ErrorActionPreference = "Stop"

kubectl delete job policy-management-schema-v056 -n refund-platform --ignore-not-found
kubectl delete configmap policy-management-schema-v056 -n refund-platform --ignore-not-found

kubectl apply -f .\infrastructure\kubernetes\jobs\policy-management-schema-v056.yaml
kubectl wait --for=condition=complete job/policy-management-schema-v056 -n refund-platform --timeout=180s

kubectl apply -f .\infrastructure\kubernetes\base\policy-management-service.yaml
kubectl set image deployment/admin-ui admin-ui=admin-ui:0.5.6 -n refund-platform

kubectl rollout status deployment/policy-management-service -n refund-platform --timeout=180s
kubectl rollout status deployment/admin-ui -n refund-platform --timeout=120s
