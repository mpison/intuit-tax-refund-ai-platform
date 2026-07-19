# Apply v0.5.4

Extract the ZIP into the repository root.

## 1. Apply backend source and dependencies

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\apply-v0.5.4-backend.ps1
```

The script detects the existing IRS simulator directory. The packaged
canonical location is:

```text
services/integration/irs-simulator
```

## 2. Build and load

```powershell
.\scripts\build-v0.5.4.ps1
.\scripts\load-v0.5.4.ps1
```

## 3. Deploy

```powershell
.\scripts\deploy-v0.5.4.ps1
```

## 4. Port-forward

Run separately:

```powershell
kubectl port-forward -n refund-platform svc/irs-simulator 8090:8090
```

```powershell
kubectl port-forward -n refund-platform svc/irs-admin-ui 3100:80
```

Open:

```text
http://localhost:3100
```

## 5. Verify database

```sql
SELECT *
FROM irs_refund_records
ORDER BY updated_at DESC;
```

## Important

The simulator UI talks to `http://localhost:8090` by default. This is correct
for local browser testing with the port-forward command.
