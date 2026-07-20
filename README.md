# Intuit Tax Refund AI Platform

## Current Version

**v0.5.6**

## Overview

A cloud-native tax refund platform inspired by TurboTax-style customer experiences.

The platform demonstrates:

- secure customer and admin authentication
- tax return and refund tracking
- refund status synchronization with an IRS simulator
- refund ETA prediction
- refund history and lifecycle timelines
- PostgreSQL-backed simulator data
- a Spring AI policy assistant using RAG and PGVector
- separate customer, simulator, and operations interfaces
- Docker and Kubernetes deployment

This project is intended for system-design interviews, AI architecture discussions, integration testing, and local demonstrations. It is not connected to the real IRS and must not be used with real taxpayer data.

---

## Version Highlights

### v0.5.5 — Admin Portal

- Standalone `admin-ui`
- Standalone `admin-service`
- Keycloak login
- `ADMIN` realm-role enforcement
- Dashboard totals
- Refund counts by status
- Read-only customer search
- Read-only refund search
- Policy and system navigation placeholders

### v0.5.4 — IRS Simulator Console

- Standalone `irs-admin-ui`
- PostgreSQL-backed `irs-simulator`
- Create, list, retrieve, update, and delete simulator records
- Local-only simulator console
- Customer refresh synchronization

### v0.5.3 — Refund Timeline and History

- Refund lifecycle timeline
- PostgreSQL refund-history table and trigger
- Customer-visible recent activity
- Improved prediction and empty states

### v0.5.2 — Filed Return Workflow

- Filed-return creation
- Support for newly registered customers
- External refund reference
- Filing method
- Customer dashboard integration

### v0.5.1 — Registration and Profile

- Customer self-registration through Keycloak
- Customer profile page
- Application user synchronization
- Profile update API

---

## Major Features

### Customer Experience

- Keycloak login and registration
- Customer profile management
- Add a filed tax return
- View latest refund status
- View refund amount and tax year
- Refresh status from the IRS simulator
- View refund lifecycle timeline
- View refund status history
- View predicted refund date and confidence
- Ask refund-policy questions through a floating AI chatbot

### IRS Simulator

- Persistent PostgreSQL simulator records
- Create demo refund records
- Search and list records
- Advance simulated IRS status
- Set an official refund date
- Delete simulator records
- Preserve external-system separation from customer refund tables

### Admin Portal

- Keycloak-protected admin login
- `ADMIN` role authorization
- Platform dashboard metrics
- Registered-user search
- Refund search
- Refund counts by status
- Full policy management
- System-operations placeholder

### AI and RAG

- Spring AI integration
- Ollama local LLM support
- PGVector embeddings and retrieval
- Tax-policy document ingestion (PDF, DOCX, TXT)
- Policy-grounded chatbot responses
- Refund ETA prediction service

---

## High-Level Architecture

```text
                                +----------------------+
                                |      Keycloak        |
                                | Identity and RBAC    |
                                +----------+-----------+
                                           |
                   +-----------------------+-----------------------+
                   |                       |                       |
                   v                       v                       v
          +----------------+      +----------------+      +----------------+
          |  customer-ui   |      |    admin-ui    |      | irs-admin-ui   |
          | Port 3000      |      | Port 3200      |      | Port 3100      |
          +-------+--------+      +-------+--------+      +-------+--------+
                  |                       |                       |
                  v                       v                       v
     +--------------------------+  +--------------+      +-----------------+
     | refund-status-service    |  | admin-service|      | irs-simulator   |
     | Port 8080                |  | Port 8050    |      | Port 8090       |
     +----+----------+----------+  +------+-------+      +--------+--------+
          |          |                    |                       |
          |          |                    +-----------+-----------+
          |          |                                |
          |          v                                v
          |   +----------------------+        +----------------------+
          |   | refund-prediction    |        | PostgreSQL           |
          |   | Port 8070            |        | Port 5432            |
          |   +----------------------+        |                      |
          |                                    | app_users            |
          |                                    | tax_returns          |
          |                                    | refund_statuses      |
          |                                    | refund_status_history|
          |                                    | irs_refund_records   |
          |                                    | PGVector data        |
          |                                    +----------------------+
          |
          v
+------------------------------+
| tax-policy-assistant-service |
| Port 8060                    |
+---------------+--------------+
                |
                +--------------------+
                |                    |
                v                    v
          +-----------+        +-----------+
          | PGVector  |        |  Ollama   |
          | PostgreSQL|        | Port 11434|
          +-----------+        +-----------+
```

---

## Core Workflows

### Customer Refund Refresh

```text
Customer
  -> customer-ui
  -> refund-status-service
  -> irs-simulator
  -> irs_refund_records
  -> refund-status-service
  -> refund_statuses
  -> refund_status_history
  -> customer dashboard
```

### IRS Simulation

```text
Demo operator
  -> irs-admin-ui
  -> irs-simulator
  -> irs_refund_records
```

The IRS Simulator represents external state. The customer application does not write directly to `irs_refund_records`.

### Admin Operations

```text
Admin user
  -> admin-ui
  -> Keycloak
  -> JWT with ADMIN role
  -> admin-service
  -> PostgreSQL read models
```

### Policy Assistant

```text
Customer
  -> floating chatbot
  -> tax-policy-assistant-service
  -> PGVector retrieval
  -> Ollama
  -> grounded answer
```

---

## Repository Structure

```text
applications/
  customer-ui/
  irs-admin-ui/
  admin-ui/

services/
  core/
    refund-status-service/
  integrations/
    irs-simulator/
  admin/
    admin-service/
  ai/
    refund-prediction-service/
    tax-policy-assistant-service/

infrastructure/
  keycloak/
  kubernetes/
    base/
    overlays/
    jobs/

scripts/

docs/
  01-architecture/
  04-kubernetes/
  05-database/
  05-security/
  06-testing/
  07-release-notes/
```

Some service directories may differ slightly depending on the local repository layout.

---

## Technology Stack

### Frontend

- React
- Vite
- Keycloak JavaScript adapter
- Nginx

### Backend

- Java 21
- Spring Boot 4
- Spring Security
- OAuth2 Resource Server
- Spring JDBC
- Spring AI

### Data and AI

- PostgreSQL
- PGVector
- Redis for planned or optional chat memory
- Ollama
- Local embedding models
- Refund prediction service

### Infrastructure

- Docker
- Kubernetes
- Kind
- Kustomize
- PowerShell

---

## Service and UI Ports

| Component | Local Port | Purpose |
|---|---:|---|
| Customer UI | 3000 | Customer refund experience |
| IRS Simulator Console | 3100 | Local simulator operations |
| Admin UI | 3200 | Secured operations portal |
| Admin Service | 8050 | Admin APIs |
| Policy Management Service | 8040 | Policy administration APIs |
| Tax Policy Assistant Service | 8060 | RAG and policy chatbot |
| Refund Prediction Service | 8070 | Refund ETA prediction |
| Refund Status Service | 8080 | Customer refund APIs |
| Keycloak | 8081 | Authentication and authorization |
| IRS Simulator | 8090 | Simulated external IRS API |
| PostgreSQL | 5432 | Transactional data and PGVector |
| Redis | 6379 | Optional chat/session memory |
| Ollama | 11434 | Local LLM and embeddings |

Kubernetes services can reuse the same internal service port because each has its own ClusterIP. Local port-forward ports must be unique.

---

## Current Image Versions

| Image | Version |
|---|---|
| customer-ui | 0.5.3 |
| refund-status-service | 0.5.3 |
| irs-admin-ui | 0.5.4 |
| irs-simulator | 0.5.4 |
| admin-ui | 0.5.5 |
| admin-service | 0.5.5 |
| policy-management-service | 0.5.6 |
| refund-prediction-service | 0.4.2 |
| tax-policy-assistant-service | 0.5.6 |

Update this table when a component is rebuilt with a newer version.

---

## Prerequisites

- Windows PowerShell
- Java 21+
- Maven 3.9+
- Docker Desktop
- Kind
- kubectl
- Ollama
- Git

Optional:

- IntelliJ IDEA
- Postman
- pgAdmin

---

## Ollama Setup

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
ollama list
```

Use the models configured in the Tax Policy Assistant service.

---

## Keycloak Setup

The platform uses Keycloak for authentication and role-based authorization.

Realm:

```text
refund-platform
```

Realm roles:

```text
CUSTOMER
ADMIN
IRS_AGENT
SYSTEM
```

Frontend clients:

| Client | Redirect URI | Web Origin |
|---|---|---|
| customer-ui | `http://localhost:3000/*` | `http://localhost:3000` |
| admin-ui | `http://localhost:3200/*` | `http://localhost:3200` |

Both frontend clients should be public OpenID Connect clients using authorization code flow with PKCE.

The admin user must have the `ADMIN` realm role. Sign out and sign back in after assigning a new role so the access token contains the updated role mapping.

Detailed guides:

- [Keycloak Setup](docs/05-security/KEYCLOAK_SETUP.md)
- [Keycloak Administration](docs/05-security/KEYCLOAK_ADMIN_GUIDE.md)
- [Keycloak Architecture](docs/05-security/KEYCLOAK_ARCHITECTURE.md)

A future improvement is to maintain a sanitized realm export under:

```text
infrastructure/keycloak/refund-platform-realm.json
```

Do not commit real passwords, secrets, access tokens, or private client credentials.

---

## Database Model Summary

### Customer Platform Tables

```text
app_users
tax_returns
refund_statuses
refund_status_history
```

### IRS Simulator Table

```text
irs_refund_records
```

### AI and RAG Data

PGVector tables store policy chunks and embeddings as configured by Spring AI.

### Important Logical Relationship

```text
tax_returns.external_refund_id
    =
irs_refund_records.external_refund_id
```

There is intentionally no foreign key between these tables because the IRS Simulator represents an external system boundary.

---

## Database Initialization

The current v0.5.x demo uses Kubernetes bootstrap SQL and idempotent schema Jobs.

Flyway migration files may be retained for a future infrastructure refactor, but Flyway is not the primary database initializer in v0.5.x.

Planned v0.6+ cleanup:

- create a complete Flyway baseline
- remove schema-changing bootstrap init containers
- support greenfield database provisioning
- maintain repeatable versioned migrations

---

## Build

### Build v0.5.4 Simulator Components

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\scripts\build-v0.5.4.ps1
.\scripts\load-v0.5.4.ps1
.\scripts\deploy-v0.5.4.ps1
```

### Build v0.5.5 Admin Portal

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\scripts\build-v0.5.5.ps1
.\scripts\load-v0.5.5.ps1
.\scripts\deploy-v0.5.5.ps1
```

### Build an Individual Service

```powershell
mvn `
  -f .\services\admin\admin-service\pom.xml `
  clean package
```

```powershell
docker build `
  --no-cache `
  -t admin-service:0.5.5 `
  .\services\admin\admin-service
```

```powershell
kind load docker-image admin-service:0.5.5 `
  --name refund-demo
```

---

## Kubernetes Deployment

Validate Kustomize:

```powershell
kubectl kustomize infrastructure\kubernetes\overlays\local > $null
```

Apply the platform:

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
```

Apply standalone manifests when they are not yet included in the base Kustomization:

```powershell
kubectl apply `
  -f .\infrastructure\kubernetes\base\admin-service.yaml

kubectl apply `
  -f .\infrastructure\kubernetes\base\admin-ui.yaml

kubectl apply `
  -f .\infrastructure\kubernetes\base\irs-admin-ui.yaml
```

Verify:

```powershell
kubectl get pods -n refund-platform
kubectl get services -n refund-platform
```

---

## Port Forwarding

Run each command in a separate terminal as needed.

```powershell
kubectl port-forward -n refund-platform svc/customer-ui 3000:80
```

```powershell
kubectl port-forward -n refund-platform svc/irs-admin-ui 3100:80
```

```powershell
kubectl port-forward -n refund-platform svc/admin-ui 3200:80
```

```powershell
kubectl port-forward -n refund-platform svc/admin-service 8050:8050
```

```powershell
kubectl port-forward -n refund-platform svc/tax-policy-assistant-service 8060:8060
```

```powershell
kubectl port-forward -n refund-platform svc/refund-prediction-service 8070:8070
```

```powershell
kubectl port-forward -n refund-platform svc/refund-status-service 8080:8080
```

```powershell
kubectl port-forward -n refund-platform svc/keycloak 8081:8080
```

```powershell
kubectl port-forward -n refund-platform svc/irs-simulator 8090:8090
```

```powershell
kubectl port-forward -n refund-platform svc/postgres 5432:5432
```

---

## Local URLs

| Application | URL |
|---|---|
| Customer Portal | `http://localhost:3000` |
| IRS Simulator Console | `http://localhost:3100` |
| Admin Portal | `http://localhost:3200` |
| Admin Service Health | `http://localhost:8050/actuator/health` |
| Tax Policy Assistant API | `http://localhost:8060` |
| Refund Prediction API | `http://localhost:8070` |
| Refund Status API | `http://localhost:8080` |
| Keycloak | `http://localhost:8081` |
| IRS Simulator API | `http://localhost:8090` |

---

## API Summary

### Customer Refund APIs

```http
GET /api/v1/refunds/latest
POST /api/v1/refunds/refresh
GET /api/v1/refunds/{taxReturnId}/history
POST /api/v1/tax-returns
GET /api/v1/users/me
PUT /api/v1/users/me
```

### IRS Simulator APIs

```http
GET /api/v1/irs/refunds/{externalRefundId}
GET /api/v1/demo/irs/refunds
GET /api/v1/demo/irs/refunds/{externalRefundId}
POST /api/v1/demo/irs/refunds
POST /api/v1/demo/irs/refunds/{externalRefundId}/status
DELETE /api/v1/demo/irs/refunds/{externalRefundId}
```

### Admin APIs

```http
GET /api/v1/admin/dashboard
GET /api/v1/admin/users
GET /api/v1/admin/refunds
```

### Policy Management APIs

```http
POST   /api/v1/admin/policies
GET    /api/v1/admin/policies
DELETE /api/v1/admin/policies/{policyDocumentId}
POST   /api/v1/admin/policies/{policyDocumentId}/reindex
```

Admin APIs require an access token containing the `ADMIN` realm role.

---

## Policy Ingestion

Ingest default policy documents:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8060/api/v1/admin/policies/ingest-defaults"
```

Use synthetic or public policy documents only. Do not ingest confidential or personal tax records.

---

## Demo Accounts and Data

Create demo users through Keycloak.

Recommended examples:

```text
Customer user:
mel.demo@example.com
Role: CUSTOMER

Admin user:
admin.demo@example.com
Role: ADMIN
```

Use temporary demo passwords and do not commit them.

Recommended synthetic external refund ID:

```text
IRS-DEMO-2025-0001
```

---

## End-to-End Demo

### Customer Flow

1. Sign in or register through the Customer Portal.
2. Open Profile and verify application-user synchronization.
3. Add a filed return.
4. View the refund dashboard.
5. View the prediction card and timeline.
6. Ask a policy question through the chatbot.

### IRS Simulation Flow

1. Open the IRS Simulator Console.
2. Select the same `external_refund_id` used by the customer return.
3. Change the status from `FILED` to `PROCESSING`.
4. Return to the Customer Portal.
5. Click **Refresh from IRS**.
6. Verify the dashboard and history update.
7. Repeat for `APPROVED`, `REFUND_SENT`, and `REFUND_RECEIVED`.

### Admin Flow

1. Open the Admin Portal.
2. Sign in with an `ADMIN` user.
3. Review dashboard metrics.
4. Search registered users.
5. Search refund records.
6. Verify that a non-admin user receives access denied.

---

## Security Notes

- The Customer Portal and Admin Portal use Keycloak.
- Admin APIs require the `ADMIN` realm role.
- The IRS Simulator Console intentionally has no login because it is a local-only demo mechanism.
- Do not expose `irs-admin-ui` through public ingress.
- Do not commit `.env` files, tokens, passwords, or secrets.
- Use HTTPS, secret management, audit logs, and stricter CORS policies in production.
- Use synthetic tax data only.

---

## Observability

Current health endpoints are exposed through Spring Boot Actuator where configured.

Examples:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8050/actuator/health"
```

```powershell
kubectl logs deployment/admin-service `
  -n refund-platform `
  --tail=200
```

```powershell
kubectl logs deployment/refund-status-service `
  -n refund-platform `
  --tail=200
```

Planned production observability includes:

- Prometheus metrics
- Grafana dashboards
- structured logs
- distributed tracing
- SLOs and error budgets
- alerting and runbooks

---

## Troubleshooting

### Client Not Found

Verify the Keycloak realm and client ID:

```text
Realm: refund-platform
Client ID: admin-ui
```

### Admin Portal Returns 403

- Assign the `ADMIN` realm role.
- Sign out.
- Sign back in to obtain a new token.

### ImagePullBackOff

```powershell
docker images admin-service
docker images admin-ui

kind load docker-image admin-service:0.5.5 --name refund-demo
kind load docker-image admin-ui:0.5.5 --name refund-demo
```

### Incorrect Service Port

```powershell
kubectl get svc -n refund-platform
```

Expected admin-service port:

```text
8050/TCP
```

### Pod Fails Readiness

```powershell
kubectl describe pod `
  -n refund-platform `
  -l app=admin-service

kubectl logs deployment/admin-service `
  -n refund-platform `
  --tail=200
```

### IRS Record Not Found

Confirm:

```text
tax_returns.external_refund_id
```

matches:

```text
irs_refund_records.external_refund_id
```

### Browser Cannot Reach a Backend

Verify the relevant port-forward terminal is still running.

---

## Documentation

### Architecture

- [IRS Simulator Architecture](docs/01-architecture/IRS_SIMULATOR_ARCHITECTURE_v0.5.4.md)

### Kubernetes

- [IRS Simulator Deployment](docs/04-kubernetes/IRS_SIMULATOR_DEPLOYMENT_v0.5.4.md)

### Database

- [IRS Simulator Database](docs/05-database/IRS_SIMULATOR_DATABASE_v0.5.4.md)

### Security

- [Keycloak Setup](docs/05-security/KEYCLOAK_SETUP.md)
- [Keycloak Administration](docs/05-security/KEYCLOAK_ADMIN_GUIDE.md)
- [Keycloak Architecture](docs/05-security/KEYCLOAK_ARCHITECTURE.md)

### Testing

- [IRS Simulator End-to-End Test](docs/06-testing/IRS_SIMULATOR_E2E_TEST_v0.5.4.md)

### Release Notes

- [v0.5.4 Release Notes](docs/07-release-notes/RELEASE_NOTES_v0.5.4.md)
- [v0.5.5 Release Notes](docs/07-release-notes/RELEASE_NOTES_v0.5.5.md)

### Roadmap

- [ROADMAP.md](ROADMAP.md)

---

## Roadmap

### v0.5.6 — Policy Management and RAG Ingestion

- Standalone Policy Management Service
- Policy upload (PDF, DOCX, TXT)
- Policy listing
- Delete policy
- Re-index failed ingestion
- PostgreSQL policy metadata
- Local document storage
- Apache PDFBox PDF extraction
- Apache POI DOCX extraction
- Text document extraction
- Spring AI chunk generation
- PGVector embedding storage
- Chunk and embedding statistics
- End-to-end verified RAG ingestion
- Customer chatbot grounded on uploaded policy documents

### v0.5.7 — Demo Polish

- UI consistency
- loading skeletons
- accessibility
- confirmation dialogs
- demo reset scripts
- final walkthrough

### v0.6.0 — Account-Aware AI Refund Assistant

- customer-data tools
- refund-history tools
- prediction tool
- policy RAG
- conversation memory
- grounded account-aware responses

### v0.6.1 — MCP Tools

- Customer MCP server
- Refund MCP server
- Policy MCP server
- Prediction MCP server
- IRS Simulator MCP server
- tool authorization and audit

### v0.6.2 — AI Evaluation and Guardrails

- golden datasets
- RAG evaluation
- hallucination checks
- citation validation
- PII masking
- tool-call authorization tests

### v0.7.x — Production Hardening

- Flyway migration baseline
- Kafka eventing
- Redis caching
- retry and circuit breaker
- rate limiting
- distributed tracing
- SRE dashboards
- backups and disaster recovery
- security hardening

---

## Known Limitations

- Local demo only
- No real IRS integration
- IRS Simulator Console has no authentication
- Policy ingestion is synchronous
- Single active policy version is supported
- MCP tool orchestration begins in v0.5.7
- Bootstrap SQL remains the primary schema initializer for v0.5.x
- Some deployment resources are applied as standalone manifests rather than through one consolidated Kustomization

---

## License and Data Safety

This repository is a demonstration project.

- Do not use real SSNs.
- Do not use real taxpayer documents.
- Do not store production credentials.
- Do not expose local simulator or admin components publicly.