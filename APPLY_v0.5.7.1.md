# Apply v0.5.7.1

## 1. Add the dependency

Add this inside the Tax Policy Assistant `<dependencies>`:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-client</artifactId>
</dependency>
```

## 2. Merge application configuration

Merge `APPLICATION_YML_v0.5.7.1.yaml` under the existing `spring.ai` section.
Do not replace your Ollama, embedding, vector-store, datasource, or RAG settings.

## 3. Confirm MCP services

```powershell
kubectl get svc `
  -n refund-platform `
  customer-mcp `
  refund-mcp `
  prediction-mcp
```

Start with Java:

```powershell
.\scripts\switch-mcp-runtime.ps1 -Runtime java
```

## 4. Build, load, deploy

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\scripts\build-v0.5.7.1.ps1
.\scripts\load-v0.5.7.1.ps1
.\scripts\deploy-v0.5.7.1.ps1
```

## 5. Verify startup

```powershell
kubectl logs `
  deployment/tax-policy-assistant-service `
  -n refund-platform `
  --tail=300
```

There should be no MCP connection, 404, or initialization errors.

## 6. Test discovery

```powershell
kubectl port-forward `
  -n refund-platform `
  svc/tax-policy-assistant-service `
  8060:8060
```

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8060/api/v1/ai/mcp/tools"
```

Expected tool count: `4`.

Logical tools:

- `get_customer_by_identity`
- `get_latest_refund_by_identity`
- `get_refund_history_by_identity`
- `predict_refund_date`

Spring AI may prefix tool names to avoid collisions.

## 7. Regression test

Ask the existing chatbot:

```text
How long does a tax refund normally take?
```

It must still answer from the uploaded policy. MCP tools are connected but are
not yet registered with the chat flow in this revision.
