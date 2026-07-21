# Apply v0.5.7.4 — Unified MCP and Policy Tool Agent

## Purpose

Register every discovered MCP tool and the PGVector policy-search tool with one
Spring AI `ChatClient`.

Spring AI's `ToolCallingAdvisor` manages the tool-call loop automatically.

## Files

Replace:

```text
services/ai/tax-policy-assistant-service/src/main/java/
com/refundplatform/policy/service/TaxPolicyAssistantService.java
```

Add:

```text
services/ai/tax-policy-assistant-service/src/main/java/
com/refundplatform/policy/ai/tools/PolicySearchTool.java
```

Replace the Dockerfile with the wildcard-JAR version.

## Maven version

Change the real project version to:

```xml
<version>0.5.7.4</version>
```

No new dependencies are required.

## Configuration

Merge:

```text
application-v0.5.7.4-additions.yml
```

into the real `application.yml`.

Do not create duplicate top-level `spring:` or `logging:` keys.

The important effective settings are:

```yaml
spring:
  ai:
    chat:
      client:
        tool-calling:
          enabled: true
    mcp:
      client:
        version: 0.5.7.4
        toolcallback:
          enabled: true
```

## Build

```powershell
mvn `
  -f .\services\ai\tax-policy-assistant-service\pom.xml `
  clean package
```

## Build, load, deploy

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\scripts\build-v0.5.7.4.ps1
.\scripts\load-v0.5.7.4.ps1
.\scripts\deploy-v0.5.7.4.ps1
```

## Restart port-forward

```powershell
kubectl port-forward `
  -n refund-platform `
  svc/tax-policy-assistant-service `
  8060:8060
```

## Test policy tool

Ask:

```text
How long does a tax refund normally take?
```

Expected:

```text
model calls search_tax_policy
-> PGVector returns policy matches
-> final answer cites the returned policy title
```

## Test prediction MCP

Use a real tax-return UUID:

```text
Predict the expected refund date for tax return
<REAL_TAX_RETURN_UUID> using 21 policy-processing days.
```

Expected:

```text
model calls predict_refund_date
-> final answer labels the date official or predicted
```

## Test multi-tool behavior

When valid customer identity context becomes available, ask:

```text
When should I receive my refund, and how does that compare with the
standard processing policy?
```

Target flow:

```text
Customer MCP
-> Refund MCP
-> Prediction MCP
-> search_tax_policy
-> combined answer
```

## Verify tool execution

Enable and inspect logs:

```powershell
kubectl logs `
  deployment/tax-policy-assistant-service `
  -n refund-platform `
  --since=10m |
Select-String "tool|Tool|MCP|search_tax_policy"
```

## Important security limitation

The current request payload contains only:

```text
conversationId
question
```

It does not provide a trusted authenticated subject.

Do not instruct users to paste an external identity ID into chat. JWT-derived
identity propagation should be added before exposing customer-specific tools in
a production environment.

## Acceptance criteria

- project compiles against Spring AI 2.0.0;
- all MCP callbacks are registered;
- `search_tax_policy` is registered as a local tool;
- policy questions invoke PGVector through the tool;
- prediction questions can invoke Prediction MCP;
- Spring AI produces one final answer after its tool-call loop;
- controller and UI payloads remain unchanged.
