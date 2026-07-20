# Apply v0.5.6

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-v0.5.6.ps1
.\scripts\load-v0.5.6.ps1
.\scripts\deploy-v0.5.6.ps1
```

Port-forward:

```powershell
kubectl port-forward -n refund-platform svc/policy-management-service 8040:8040
kubectl port-forward -n refund-platform svc/admin-ui 3200:80
```

Required Tax Policy Assistant ingestion endpoint:

```http
POST /api/v1/admin/policies/ingest
Content-Type: multipart/form-data
Part name: file
```
