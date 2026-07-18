
# Release Notes v0.3.0

## Summary

Introduces the Refund Prediction Service, enabling estimated refund arrival dates using a baseline prediction model.

## New
- Prediction microservice
- Prediction REST API
- Customer UI prediction card
- Kubernetes deployment

## Upgrade

Build:

```powershell
.\scripts\build-images-v0.3.ps1
```

Deploy:

```powershell
kubectl apply -k infrastructure\kubernetes\overlays\local
```

## Next
v0.4.0 will introduce Spring AI, Ollama, RAG, and Tax Policy Assistant.
