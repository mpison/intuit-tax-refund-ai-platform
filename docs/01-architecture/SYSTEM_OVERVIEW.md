# System Overview

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

Design goals:
- Clear service boundaries
- Secure access
- Independent deployment
- Local AI development
- Kubernetes-ready operations
