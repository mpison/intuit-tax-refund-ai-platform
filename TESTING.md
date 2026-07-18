
# Testing Guide

## Verify

```powershell
kubectl get pods -n refund-platform
kubectl get deployments -n refund-platform
```

## Prediction

```powershell
kubectl port-forward -n refund-platform svc/refund-prediction-service 8070:8070
```

POST `/api/v1/predictions/refund-eta`

Verify:
- predictedRefundDate
- estimatedDaysRemaining
- confidenceScore
- explanation

## UI
- Login
- Refund page
- Timeline
- Prediction card
- Refresh from IRS

## Database
Verify:
- app_users
- tax_returns
- refund_statuses
- refund_status_history
