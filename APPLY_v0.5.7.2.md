# Apply v0.5.7.2 — Spring AI Tool Foundation

## 1. Merge the Java files

New package root:

```text
com.refundplatform.policy.ai
```

Added:

```text
ai/config
ai/context
ai/orchestration
ai/prompt
ai/rag
ai/tools
```

## 2. Confirm dependency

The Tax Policy Assistant `pom.xml` must contain:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-client</artifactId>
</dependency>
```

Spring AI must remain at version `2.0.0`.

## 3. Confirm MCP callbacks

Keep this in the real `application.yml`:

```yaml
spring:
  ai:
    mcp:
      client:
        enabled: true
        initialized: true
        toolcallback:
          enabled: true
```

Do not replace existing model, embedding, PGVector, datasource, or RAG settings.

## 4. Build

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-v0.5.7.2.ps1
```

## 5. Load and deploy

```powershell
.\scripts\load-v0.5.7.2.ps1
.\scripts\deploy-v0.5.7.2.ps1
```

## 6. Verify

```powershell
.\scripts\verify-v0.5.7.2.ps1
```

## 7. Regression test

Restart the local port-forward if needed:

```powershell
kubectl port-forward `
  -n refund-platform `
  svc/tax-policy-assistant-service `
  8060:8060
```

Ask:

```text
How long does a tax refund normally take?
```

Expected:

- policy-grounded answer;
- source attribution;
- no MCP tool invocation.

## Success criteria

- project compiles against Spring AI 2.0.0;
- Tax Policy Assistant starts;
- MCP `ToolCallbackProvider` beans remain internally available;
- existing RAG chat remains unchanged;
- no temporary diagnostics API is introduced.
