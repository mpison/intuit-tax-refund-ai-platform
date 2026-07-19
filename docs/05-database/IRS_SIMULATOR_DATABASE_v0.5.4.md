# IRS Simulator Database — v0.5.4

## Table

```text
irs_refund_records
```

## Purpose

Stores simulated external IRS refund state.

## Schema

```sql
CREATE TABLE IF NOT EXISTS irs_refund_records (
    external_refund_id VARCHAR(100) PRIMARY KEY,
    status VARCHAR(40) NOT NULL,
    official_refund_date DATE,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

## Logical Relationship

```text
tax_returns.external_refund_id
    =
irs_refund_records.external_refund_id
```

There is intentionally no database foreign key because the IRS Simulator represents an external system boundary.

## State Separation

| Table | Meaning |
|---|---|
| `irs_refund_records` | external simulator state |
| `refund_statuses` | latest internal synchronized state |
| `refund_status_history` | internal transition history |

## Verify

```sql
SELECT
    external_refund_id,
    status,
    official_refund_date,
    updated_at
FROM irs_refund_records
ORDER BY updated_at DESC;
```

Use synthetic IDs only, such as `IRS-DEMO-2025-0001`.
