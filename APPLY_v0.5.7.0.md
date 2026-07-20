# Apply v0.5.7.0

## Build

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-v0.5.7.0-mcp.ps1
```

## Load

```powershell
.\scripts\load-v0.5.7.0-mcp.ps1
```

## Deploy

```powershell
.\scripts\deploy-v0.5.7.0-mcp.ps1
```

Both Java and Python deployments are started. Stable Kubernetes Services
initially route to Java.

## Switch all domains

```powershell
.\scripts\switch-mcp-runtime.ps1 -Runtime python
.\scripts\switch-mcp-runtime.ps1 -Runtime java
```

## Mixed runtime example

```powershell
.\scripts\switch-mcp-runtime.ps1 `
  -Runtime python `
  -Domain prediction
```

This allows:

```text
customer MCP   -> Java
refund MCP     -> Java
prediction MCP -> Python
```

## Stable endpoints

```text
http://customer-mcp:8030/mcp
http://refund-mcp:8031/mcp
http://prediction-mcp:8032/mcp
```

## Test with MCP Inspector

Port-forward:

```powershell
kubectl port-forward `
  -n refund-platform `
  svc/customer-mcp `
  8030:8030
```

Connect the MCP Inspector to:

```text
http://localhost:8030/mcp
```

v0.5.7.0 creates the MCP providers only. Tax Policy Assistant MCP-client
integration, JWT propagation, and personalized answers are subsequent
v0.5.7 revisions.
