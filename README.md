
# Intuit Tax Refund AI Platform

## Version
**v0.3.0**

## Overview
Cloud-native microservices demo for secure tax refund tracking with:
- Customer UI (React)
- Keycloak authentication
- Refund Status Service
- IRS Simulator
- Refund Prediction Service
- PostgreSQL
- Kubernetes (Kind)

## Architecture

```text
Customer UI
    |
Keycloak
    |
Refund Status Service
   |    |  +--> IRS Simulator
   |
   +--> Refund Prediction Service
   |
PostgreSQL
```

## Components
- customer-ui
- refund-status-service
- refund-prediction-service
- irs-simulator
- postgres
- keycloak

## Docker Images
| Image | Version |
|---|---|
| customer-ui | 0.3.0 |
| refund-status-service | 0.3.0 |
| refund-prediction-service | 0.3.0 |
| irs-simulator | 0.2.0 |

## Build

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-images-v0.3.ps1
```

## Load

```powershell
.\scripts\load-images-v0.3.ps1
```

## Deploy

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
```

## Roadmap

- v0.1.0 Core Platform
- v0.2.0 IRS Simulator
- v0.3.0 Prediction Service
- v0.4.0 Spring AI + RAG
- v0.5.0 MCP
- v0.6.0 AI Gateway
- v0.7.0 Kafka
- v0.8.0 Observability
- v0.9.0 Production Hardening
- v1.0.0 Production Release
