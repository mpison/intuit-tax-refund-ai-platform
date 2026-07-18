# v0.2.0 Testing

## Health

```powershell
Invoke-RestMethod http://localhost:8090/actuator/health
Invoke-RestMethod http://localhost:8080/actuator/health
```

## IRS Lookup

```powershell
Invoke-RestMethod `
  http://localhost:8090/api/v1/irs/refunds/IRS-DEMO-2025-0001
```

## Update IRS Status

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

## UI Test

1. Open `http://localhost:3000`.
2. Login.
3. Click **Refresh from IRS**.
4. Confirm status changes to **Approved**.
5. Confirm the timeline advances.

## Database History

```powershell
kubectl exec -it postgres-0 `
  -n refund-platform `
  -- psql `
  -U refund_user `
  -d refund_platform `
  -c "SELECT * FROM refund_status_history ORDER BY changed_at DESC;"
```
