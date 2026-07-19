# Testing Guide

## Kubernetes
```powershell
kubectl get pods -n refund-platform
kubectl get deployments -n refund-platform
```

## Authentication
```powershell
$response = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/realms/refund-platform/protocol/openid-connect/token" `
  -ContentType "application/x-www-form-urlencoded" `
  -Body @{
    client_id="customer-ui"
    grant_type="password"
    username="mel.demo"
    password="Demo123!"
  }
$accessToken = $response.access_token
```

## Latest Refund
```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8080/api/v1/refunds/latest" `
  -Headers @{ Authorization = "Bearer $accessToken" }
```

## IRS Simulator
```powershell
Invoke-RestMethod http://localhost:8090/api/v1/irs/refunds/IRS-DEMO-2025-0001
```

## Prediction
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8070/api/v1/predictions/refund-eta" `
  -ContentType "application/json" `
  -Body '{"taxYear":2025,"filedAt":"2026-04-10T10:00:00Z","currentStatus":"PROCESSING","refundAmount":2850.00,"lastExternalSyncAt":"2026-07-18T02:00:00Z"}'
```

## Policy Ingestion
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8060/api/v1/admin/policies/ingest-defaults"
```

## Policy Chat
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8060/api/v1/assistant/chat" `
  -ContentType "application/json" `
  -Body '{"conversationId":"test-1","question":"Why can a refund be delayed?"}'
```

## UI Regression
- Login works.
- Refund timeline renders.
- Prediction card renders.
- IRS refresh works.
- Floating chatbot opens and closes.
- Chat history persists.
- Clear history works.
- Blue theme is visible.


# Testing v0.5.2

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scriptspply-v0.5.2-project-updates.ps1
.\scriptsuild-images-v0.5.2.ps1
.\scripts\load-images-v0.5.2.ps1
kubectl apply -k infrastructure\kubernetes\overlays\local
```

Verify:

```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

Expected: baseline version 3 and successful versions 4 and 5.

# Testing v0.5.2

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scriptspply-v0.5.2-project-updates.ps1
.\scriptsuild-images-v0.5.2.ps1
.\scripts\load-images-v0.5.2.ps1
kubectl apply -k infrastructure\kubernetes\overlays\local
```

Verify:

```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

Expected: baseline version 3 and successful versions 4 and 5.
