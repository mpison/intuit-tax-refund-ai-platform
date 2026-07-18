# Start Here — v0.2.0

## 1. Extract into your repository

Copy this release's folders into your existing repository root:

```text
C:\Users\Melchedick Pison\AIProjects\intuit-tax-refund-ai-platform
```

Allow Windows to merge folders and replace files.

## 2. Remove obsolete v0.2 patch files

See:

```text
MIGRATION_FROM_V0.1.md
```

## 3. Build

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-images-v0.2.ps1
```

## 4. Load

```powershell
.\scripts\load-images-v0.2.ps1
```

## 5. Deploy

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
```

## 6. Verify

```powershell
kubectl get pods -n refund-platform -w
```
