# Kubernetes Deployment

```powershell
kubectl kustomize infrastructure\kubernetes\overlays\local > $null
kubectl apply -k infrastructure\kubernetes\overlays\local
kubectl get pods -n refund-platform -w
```

Verify images:
```powershell
kubectl get deployments -n refund-platform `
  -o custom-columns="DEPLOYMENT:.metadata.name,IMAGE:.spec.template.spec.containers[0].image"
```
