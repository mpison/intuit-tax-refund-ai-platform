# Start Here

Run commands from the project root.

## Build

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-images.ps1
```

## Load images

```powershell
kind load docker-image refund-status-service:0.1.4 --name refund-demo
kind load docker-image customer-ui:0.1.0 --name refund-demo
```

## Deploy

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
kubectl get pods -n refund-platform
```

## Port-forward

```powershell
kubectl port-forward -n refund-platform svc/keycloak 8081:8080
kubectl port-forward -n refund-platform svc/refund-status-service 8080:8080
kubectl port-forward -n refund-platform svc/customer-ui 3000:80
```

Open `http://localhost:3000`.

Credentials:

- Username: `mel.demo`
- Password: `Demo123!`
