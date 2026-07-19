# v0.5.3 Backend Changes

## Added

- Customer-owned refund history endpoint:
  `GET /api/v1/refunds/{taxReturnId}/history`
- Refund history response DTOs.
- Refund history service using the existing PostgreSQL database.
- Idempotent Kubernetes schema Job.
- PostgreSQL trigger that records refund status inserts and transitions.
- Backfill of one current-status event for returns without history.

## Security

The endpoint joins the tax return to `app_users` and requires the JWT subject
to match `external_identity_id`. A customer cannot read another customer's
refund history.
