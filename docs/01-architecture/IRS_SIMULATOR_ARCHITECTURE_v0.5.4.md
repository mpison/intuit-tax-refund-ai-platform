# IRS Simulator Architecture — v0.5.4

## Purpose

The IRS Simulator models an external refund-processing system for local demos and integration tests.

## Components

### IRS Simulator Console

Location:

```text
applications/irs-admin-ui
```

Responsibilities:

- create, list, edit, and delete demo IRS records
- set simulated status
- set an optional official refund date

### IRS Simulator Service

Location:

```text
services/integrations/irs-simulator
```

Responsibilities:

- serve customer lookup requests
- serve demo-management APIs
- validate statuses
- persist simulator records

### Data Ownership

| Data | Owner |
|---|---|
| Customer identity | Keycloak |
| Customer profile and tax return | Refund Platform |
| Internal refund status | Refund Platform |
| Simulated IRS state | IRS Simulator |
| Refund history | Refund Platform |

## Synchronization

```text
IRS Admin UI
  -> IRS Simulator
  -> irs_refund_records

Customer clicks Refresh from IRS
  -> Refund Status Service
  -> IRS Simulator API
  -> irs_refund_records
  -> refund_statuses
  -> refund_status_history
```

The two systems are eventually consistent. A simulator update is not visible to the customer until the customer platform refreshes.

## Why Separate the Systems?

- realistic external integration boundary
- clear ownership of external versus internal state
- easier retry, timeout, and circuit-breaker testing
- safer architecture than writing directly into customer tables

## Production Boundary

The demo simulator must be replaced by a secured IRS adapter in production.
