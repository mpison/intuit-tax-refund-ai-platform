# Refund Prediction Service

v0.3.0 introduces a deterministic ETA baseline behind an interface.

Later versions can replace it with:
- a trained regression model,
- SageMaker,
- Vertex AI,
- MLflow,
- or another inference endpoint.

API:

```text
POST /api/v1/predictions/refund-eta
```
