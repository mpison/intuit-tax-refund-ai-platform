# Apply v0.2.0 Patch

## 1. Copy files

Copy the patch contents into the same relative paths under:

```text
C:\Users\Melchedick Pison\AIProjects\intuit-tax-refund-ai-platform
```

## 2. Add the IRS environment variable

Open:

```text
infrastructure\kubernetes\base\refund-status-service.yaml
```

Inside the main container `env:` list, add:

```yaml
- name: IRS_SIMULATOR_BASE_URL
  value: http://irs-simulator:8090
```

Update the main image to:

```yaml
image: refund-status-service:0.2.0
```

Update the UI image to:

```yaml
image: customer-ui:0.2.0
```

## 3. Build

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-images-v0.2.ps1
```

## 4. Load into Kind

```powershell
kind load docker-image refund-status-service:0.2.0 --name refund-demo
kind load docker-image customer-ui:0.2.0 --name refund-demo
kind load docker-image irs-simulator:0.2.0 --name refund-demo
```

## 5. Apply

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
```

## 6. Verify

```powershell
kubectl get pods -n refund-platform
```

Expected new pod:

```text
irs-simulator-...   1/1   Running
```

## 7. Port-forward IRS Simulator

```powershell
kubectl port-forward -n refund-platform svc/irs-simulator 8090:8090
```

## 8. Test IRS Simulator

```powershell
Invoke-RestMethod `
  http://localhost:8090/api/v1/irs/refunds/IRS-DEMO-2025-0001
```

Change status:

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

## 9. Test Refund Refresh

Use the authenticated Refund API:

```text
POST /api/v1/refunds/{taxReturnId}/refresh
```

The current patch calls the IRS Simulator and returns the refreshed external
status. Persistence into `refund_statuses` is the next small follow-up.
