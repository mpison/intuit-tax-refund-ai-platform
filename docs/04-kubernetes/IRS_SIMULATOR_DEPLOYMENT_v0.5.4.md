# IRS Simulator Kubernetes Deployment — v0.5.4

## Workloads

```text
Deployment/irs-simulator
Service/irs-simulator
Deployment/irs-admin-ui
Service/irs-admin-ui
Job/irs-simulator-schema-v054
ConfigMap/irs-simulator-schema-v054
```

## Images

```text
irs-simulator:0.5.4
irs-admin-ui:0.5.4
```

## Build and Deploy

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\scripts\build-v0.5.4.ps1
.\scripts\load-v0.5.4.ps1
.\scripts\deploy-v0.5.4.ps1
```

## Verify

```powershell
kubectl get pods -n refund-platform
kubectl get services -n refund-platform
```

## Port Forwarding

```powershell
kubectl port-forward -n refund-platform svc/irs-simulator 8090:8090
```

```powershell
kubectl port-forward -n refund-platform svc/irs-admin-ui 3100:80
```

Open `http://localhost:3100`.

## Security

`irs-admin-ui` is intentionally not exposed through Ingress.

## Troubleshooting

```powershell
kubectl logs job/irs-simulator-schema-v054 -n refund-platform
kubectl logs deployment/irs-simulator -n refund-platform --tail=200
kubectl logs deployment/irs-admin-ui -n refund-platform --tail=200
```
