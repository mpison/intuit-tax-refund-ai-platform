# Intuit Tax Refund AI Platform

## Version

v0.2.0 — IRS Integration

## What This Release Adds

- IRS Simulator service
- Refund refresh workflow
- Refund status history persistence
- External refund identifiers
- Demo IRS status update API
- Customer UI refresh integration
- Kubernetes deployment for IRS Simulator
- Local and Kubernetes Spring profiles
- PowerShell build and deployment scripts

## Architecture

```text
Customer Browser
      |
      v
Customer UI
      |
      v
Keycloak
      |
      v
Refund Status Service
      |
      +--------------------+
      |                    |
      v                    v
PostgreSQL          IRS Simulator
```

## Services

| Service | Image | Port |
|---|---|---:|
| customer-ui | customer-ui:0.2.0 | 80 |
| refund-status-service | refund-status-service:0.2.0 | 8080 |
| irs-simulator | irs-simulator:0.2.0 | 8090 |
| keycloak | quay.io/keycloak/keycloak:26.6.0 | 8080 |
| postgres | pgvector/pgvector:pg17 | 5432 |

## Build

```powershell
Set-ExecutionPolicy `
  -Scope Process `
  -ExecutionPolicy Bypass

.\scripts\build-images-v0.2.ps1
```

## Load Images

```powershell
kind load docker-image refund-status-service:0.2.0 `
  --name refund-demo

kind load docker-image customer-ui:0.2.0 `
  --name refund-demo

kind load docker-image irs-simulator:0.2.0 `
  --name refund-demo
```

## Deploy

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
```

## Verify

```powershell
kubectl get pods -n refund-platform
```

Expected:

```text
customer-ui-...             1/1 Running
irs-simulator-...           1/1 Running
keycloak-...                1/1 Running
postgres-0                  1/1 Running
refund-status-service-...   1/1 Running
```

## Port Forward

```powershell
kubectl port-forward -n refund-platform svc/keycloak 8081:8080
kubectl port-forward -n refund-platform svc/refund-status-service 8080:8080
kubectl port-forward -n refund-platform svc/customer-ui 3000:80
kubectl port-forward -n refund-platform svc/irs-simulator 8090:8090
```

## Demo

1. Open `http://localhost:3000`.
2. Login using `mel.demo / Demo123!`.
3. Confirm refund status is `Processing`.
4. Change IRS status:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8090/api/v1/demo/irs/refunds/IRS-DEMO-2025-0001/status" `
  -ContentType "application/json" `
  -Body '{
    "status": "APPROVED",
    "officialRefundDate": "2026-07-24"
  }'
```

5. Click **Refresh from IRS** in the UI.
6. Confirm the timeline advances to **Approved**.

See `START_HERE.md`, `TESTING.md`, and `MIGRATION_FROM_V0.1.md`.
