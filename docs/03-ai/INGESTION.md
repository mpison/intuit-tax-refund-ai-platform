# Policy Ingestion

Endpoint:
```text
POST /api/v1/admin/policies/ingest-defaults
```

PowerShell:
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8060/api/v1/admin/policies/ingest-defaults"
```

The endpoint has no body and loads four built-in policies into PGVector.
