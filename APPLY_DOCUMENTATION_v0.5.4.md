# Apply v0.5.4 Documentation

Extract the ZIP into the repository root.

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\apply-v0.5.4-documentation.ps1
```

The script appends sections to `README.md`, `CHANGELOG.md`, and `ROADMAP.md` without replacing existing content.

Review:

```powershell
git status
git diff -- README.md CHANGELOG.md ROADMAP.md
```
