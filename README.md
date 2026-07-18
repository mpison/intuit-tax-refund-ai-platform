# Intuit Tax Refund AI Platform

## Current Version
**v0.4.3**

## Overview
Cloud-native tax refund platform built with React, Spring Boot, Keycloak, PostgreSQL, PGVector, Spring AI, Ollama, Docker, and Kubernetes.

## Features
- Secure login with Keycloak
- Latest refund status and amount
- Refund lifecycle timeline
- IRS Simulator refresh workflow
- Refund ETA prediction
- Spring AI RAG tax policy assistant
- PGVector document retrieval
- Floating chatbot with chat history
- Intuit/TurboTax-inspired blue theme

## Architecture
```text
Customer UI
  |-- Keycloak
  |-- Refund Status Service
  |     |-- PostgreSQL
  |     |-- IRS Simulator
  |     `-- Refund Prediction Service
  `-- Tax Policy Assistant Service
        |-- PGVector
        `-- Ollama
```

## Services
| Component | Port |
|---|---:|
| Customer UI | 3000 |
| Keycloak | 8081 |
| Refund Status Service | 8080 |
| IRS Simulator | 8090 |
| Refund Prediction Service | 8070 |
| Tax Policy Assistant Service | 8060 |
| PostgreSQL | 5432 |
| Ollama | 11434 |

## Image Versions
| Image | Version |
|---|---|
| customer-ui | 0.4.3 |
| refund-status-service | 0.4.2 |
| refund-prediction-service | 0.4.2 |
| irs-simulator | 0.4.2 |
| tax-policy-assistant-service | 0.4.1 |

## Java Package Structure
```text
config
controller
dto
exception
model
repository
service
client
```

## Prerequisites
- Java 21+
- Maven 3.9+
- Docker Desktop
- Kind
- kubectl
- PowerShell
- Ollama

## Ollama
```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
ollama list
```

## Deploy
```powershell
kubectl kustomize infrastructure\kubernetes\overlays\local > $null
kubectl apply -k infrastructure\kubernetes\overlays\local
kubectl get pods -n refund-platform
```

## Port Forwards
```powershell
kubectl port-forward -n refund-platform svc/customer-ui 3000:80
kubectl port-forward -n refund-platform svc/keycloak 8081:8080
kubectl port-forward -n refund-platform svc/refund-status-service 8080:8080
kubectl port-forward -n refund-platform svc/irs-simulator 8090:8090
kubectl port-forward -n refund-platform svc/refund-prediction-service 8070:8070
kubectl port-forward -n refund-platform svc/tax-policy-assistant-service 8060:8060
kubectl port-forward -n refund-platform svc/postgres 5432:5432
```

## Policy Ingestion
```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8060/api/v1/admin/policies/ingest-defaults"
```

## Roadmap
See [ROADMAP.md](ROADMAP.md).
