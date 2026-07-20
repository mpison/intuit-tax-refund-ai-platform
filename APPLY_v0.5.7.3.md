# Apply v0.5.7.3 — AI Intent Routing and Orchestration Planning

## Purpose

Introduce deterministic intent routing and MCP tool planning without changing
the current chatbot execution path.

## Prerequisite

v0.5.7.2 must already contain:

```text
ToolRegistry
SpringAiToolRegistry
AiExecutionContext
PromptContextAssembler
```

## Merge

Add the new classes under:

```text
com.refundplatform.policy.ai.orchestration
```

## Test

```powershell
mvn `
  -f .\servicesi	ax-policy-assistant-service\pom.xml `
  test
```

Expected:

```text
BUILD SUCCESS
```

## Build, load, deploy

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\scripts\build-v0.5.7.3.ps1
.\scripts\load-v0.5.7.3.ps1
.\scripts\deploy-v0.5.7.3.ps1
```

## Verify

```powershell
.\scripts\verify-v0.5.7.3.ps1
```

## Regression test

Restart the port-forward if needed:

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

- existing policy RAG answer;
- source attribution;
- no MCP invocation yet.

## Acceptance criteria

- intent classifier tests pass;
- account questions produce an MCP plan;
- policy questions produce a RAG-only plan;
- mixed questions produce an MCP plus RAG plan;
- required MCP tool availability is checked;
- current chatbot behavior remains unchanged.

## Next revision

v0.5.7.4 will execute the orchestration plan and build authenticated account
context for personalized refund answers.
