## IRS Simulator Console

v0.5.4 adds a standalone local IRS Simulator Console.

Components:

- `applications/irs-admin-ui`
- `services/integrations/irs-simulator`
- PostgreSQL table `irs_refund_records`

Local URLs:

- Customer UI: `http://localhost:3000`
- IRS Simulator API: `http://localhost:8090`
- IRS Simulator Console: `http://localhost:3100`

The console is local-only, has no login, and must not be exposed through public ingress.
