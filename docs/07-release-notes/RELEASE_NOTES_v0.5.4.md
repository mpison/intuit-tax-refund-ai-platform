# Release Notes v0.5.4

## Summary

v0.5.4 introduces a persistent IRS refund simulator and a standalone local IRS Simulator Console.

## Architecture

```text
Demo Operator -> irs-admin-ui -> irs-simulator -> irs_refund_records

Customer -> customer-ui -> refund-status-service -> irs-simulator
```

The simulator owns external IRS state. The customer platform synchronizes that state into `refund_statuses` and records transitions in `refund_status_history`.

## Added

- Standalone `applications/irs-admin-ui`
- PostgreSQL-backed IRS simulator
- Simulator create, list, retrieve, update, and delete APIs
- Kubernetes deployment and service for `irs-admin-ui`
- Schema initialization Job for `irs_refund_records`
- Build, load, deploy, and port-forward scripts

## Changed

- Replaced the simulator's in-memory `ConcurrentHashMap` with PostgreSQL persistence
- Preserved the customer-facing IRS lookup API

## API

- `GET /api/v1/irs/refunds/{externalRefundId}`
- `GET /api/v1/demo/irs/refunds`
- `GET /api/v1/demo/irs/refunds/{externalRefundId}`
- `POST /api/v1/demo/irs/refunds`
- `POST /api/v1/demo/irs/refunds/{externalRefundId}/status`
- `DELETE /api/v1/demo/irs/refunds/{externalRefundId}`

## Database

New table:

```sql
CREATE TABLE irs_refund_records (
    external_refund_id VARCHAR(100) PRIMARY KEY,
    status VARCHAR(40) NOT NULL,
    official_refund_date DATE,
    updated_at TIMESTAMPTZ NOT NULL
);
```

## Demo Workflow

1. Create or update a simulator record.
2. Use the same `external_refund_id` in the customer's tax return.
3. Change the simulator status.
4. Customer clicks **Refresh from IRS**.
5. The dashboard, timeline, and history update.

## Local URLs

- Customer UI: `http://localhost:3000`
- IRS Simulator API: `http://localhost:8090`
- IRS Simulator Console: `http://localhost:3100`

## Security Note

The console is local-only and intentionally has no login. Do not expose it through public ingress.

## Known Limitations

- The simulator is not connected to the real IRS.
- General user, refund, and policy administration is planned for v0.5.5 and v0.5.6.
