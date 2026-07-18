# Kubernetes Deployment

kubectl apply -k infrastructure/kubernetes/overlays/local

kubectl rollout restart deployment/refund-status-service -n refund-platform

kubectl get pods -n refund-platform
