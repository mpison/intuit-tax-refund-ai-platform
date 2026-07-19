# IRS Simulator End-to-End Test — v0.5.4

## Preconditions

- Customer UI: `http://localhost:3000`
- IRS Simulator API: `http://localhost:8090`
- IRS Simulator Console: `http://localhost:3100`

## Test

1. Create `IRS-DEMO-2025-0010` with status `FILED`.
2. Confirm the row exists in `irs_refund_records`.
3. Update it to `PROCESSING`.
4. Ensure a customer tax return uses the same `external_refund_id`.
5. Click **Refresh from IRS** in the customer UI.
6. Verify the dashboard status changes.
7. Verify the timeline advances.
8. Verify `refund_status_history` contains the transition.
9. Update to `APPROVED` with an official refund date and repeat.
10. Delete the simulator record and verify the customer refresh receives a not-found response.

## Database Verification

```sql
SELECT * FROM irs_refund_records ORDER BY updated_at DESC;
```
