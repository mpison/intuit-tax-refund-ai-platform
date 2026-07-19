# Apply v0.5.5

Create a Keycloak public client:

```text
Client ID: admin-ui
Redirect URI: http://localhost:3200/*
Web Origin: http://localhost:3200
```

Assign realm role `ADMIN` to the admin user.

Run:

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-v0.5.5.ps1
.\scripts\load-v0.5.5.ps1
.\scripts\deploy-v0.5.5.ps1
```

Port-forward:

```powershell
kubectl port-forward -n refund-platform svc/admin-service 8060:8060
```

```powershell
kubectl port-forward -n refund-platform svc/admin-ui 3200:80
```

Open `http://localhost:3200`.
