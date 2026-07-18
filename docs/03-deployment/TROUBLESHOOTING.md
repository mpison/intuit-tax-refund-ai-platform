# Troubleshooting

## PowerShell unsigned script

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
```

## Port 3000 already in use

Stop the previous port-forward or find the process:

```powershell
Get-NetTCPConnection -LocalPort 3000 -State Listen
```

## Invalid Keycloak redirect URI

Use:

```text
http://localhost:3000
```

## Verify services

```powershell
kubectl get all -n refund-platform
```
