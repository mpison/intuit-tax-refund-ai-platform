# HELP.md

# Refund Platform PostgreSQL and pgAdmin Guide

## 1. Connect pgAdmin to Kubernetes PostgreSQL

Open a dedicated PowerShell window:

```powershell
kubectl port-forward `
  -n refund-platform `
  svc/postgres `
  5432:5432
```

If port 5432 is already used:

```powershell
kubectl port-forward `
  -n refund-platform `
  svc/postgres `
  5433:5432
```

In pgAdmin, register a server with:

```text
Name: Refund Platform Local
Host: localhost
Port: 5432
Maintenance database: refund_platform
Username: refund_user
Password: refund_password
```

Use port `5433` when you forwarded `5433:5432`.

---

## 2. Verify Connection

```sql
SELECT
    current_database() AS database_name,
    current_user AS database_user,
    current_schema() AS current_schema;
```

```sql
SELECT version();
```

---

## 3. Inspect Schema

### List Tables

```sql
SELECT
    table_schema,
    table_name
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_type = 'BASE TABLE'
ORDER BY table_name;
```

Expected:

```text
app_users
tax_returns
refund_statuses
refund_status_history
```

Flyway may also create:

```text
flyway_schema_history
```

### List All Columns

```sql
SELECT
    table_name,
    ordinal_position,
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_schema = 'public'
ORDER BY table_name, ordinal_position;
```

### View Indexes

```sql
SELECT
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename, indexname;
```

### View Constraints

```sql
SELECT
    tc.table_name,
    tc.constraint_name,
    tc.constraint_type,
    kcu.column_name
FROM information_schema.table_constraints tc
LEFT JOIN information_schema.key_column_usage kcu
    ON tc.constraint_name = kcu.constraint_name
   AND tc.table_schema = kcu.table_schema
WHERE tc.table_schema = 'public'
ORDER BY tc.table_name, tc.constraint_type;
```

---

## 4. Core Data Queries

### Users

```sql
SELECT
    user_id,
    external_identity_id,
    display_name
FROM app_users
ORDER BY display_name;
```

### Tax Returns

```sql
SELECT
    tax_return_id,
    user_id,
    tax_year,
    filed_at,
    refund_amount,
    external_refund_id
FROM tax_returns
ORDER BY filed_at DESC;
```

### Refund Statuses

```sql
SELECT
    refund_status_id,
    tax_return_id,
    current_status,
    last_checked_at,
    last_external_sync_at,
    external_source
FROM refund_statuses
ORDER BY last_checked_at DESC;
```

### Status History

```sql
SELECT
    refund_status_history_id,
    tax_return_id,
    previous_status,
    new_status,
    source,
    changed_at
FROM refund_status_history
ORDER BY changed_at DESC;
```

---

## 5. Complete Dashboard Query

```sql
SELECT
    u.user_id,
    u.display_name,
    u.external_identity_id,
    tr.tax_return_id,
    tr.tax_year,
    tr.filed_at,
    tr.refund_amount,
    tr.external_refund_id,
    rs.current_status,
    rs.last_checked_at,
    rs.last_external_sync_at,
    rs.external_source
FROM app_users u
JOIN tax_returns tr
    ON tr.user_id = u.user_id
JOIN refund_statuses rs
    ON rs.tax_return_id = tr.tax_return_id
ORDER BY tr.filed_at DESC;
```

### Latest Tax Return Per User

```sql
SELECT DISTINCT ON (tr.user_id)
    tr.user_id,
    tr.tax_return_id,
    tr.tax_year,
    tr.filed_at,
    tr.refund_amount,
    tr.external_refund_id
FROM tax_returns tr
ORDER BY
    tr.user_id,
    tr.filed_at DESC;
```

### History for Demo Tax Return

```sql
SELECT
    previous_status,
    new_status,
    source,
    changed_at
FROM refund_status_history
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222'
ORDER BY changed_at;
```

---

## 6. Supported Refund Statuses

```text
FILED
ACCEPTED
PROCESSING
ADDITIONAL_REVIEW
ACTION_REQUIRED
APPROVED
REFUND_SENT
REFUND_RECEIVED
```

These values must match the Java enum.

---

## 7. Manual Simulations

### Reset to Processing

```sql
UPDATE refund_statuses
SET
    current_status = 'PROCESSING',
    last_checked_at = NOW(),
    last_external_sync_at = NOW(),
    external_source = 'MANUAL_SIMULATION'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';
```

### Simulate Approved

```sql
UPDATE refund_statuses
SET
    current_status = 'APPROVED',
    last_checked_at = NOW(),
    last_external_sync_at = NOW(),
    external_source = 'MANUAL_SIMULATION'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';
```

### Simulate Refund Sent

```sql
UPDATE refund_statuses
SET
    current_status = 'REFUND_SENT',
    last_checked_at = NOW(),
    last_external_sync_at = NOW(),
    external_source = 'MANUAL_SIMULATION'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';
```

### Simulate Action Required

```sql
UPDATE refund_statuses
SET
    current_status = 'ACTION_REQUIRED',
    last_checked_at = NOW(),
    last_external_sync_at = NOW(),
    external_source = 'MANUAL_SIMULATION'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';
```

---

## 8. Simulate a Status Change With History

Enable UUID generation when needed:

```sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;
```

```sql
BEGIN;

INSERT INTO refund_status_history (
    refund_status_history_id,
    tax_return_id,
    previous_status,
    new_status,
    source,
    changed_at
)
VALUES (
    gen_random_uuid(),
    '22222222-2222-2222-2222-222222222222',
    'PROCESSING',
    'APPROVED',
    'PGADMIN_SIMULATION',
    NOW()
);

UPDATE refund_statuses
SET
    current_status = 'APPROVED',
    last_checked_at = NOW(),
    last_external_sync_at = NOW(),
    external_source = 'PGADMIN_SIMULATION'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';

COMMIT;
```

### Automatically Capture Current Status

```sql
BEGIN;

WITH current_refund AS (
    SELECT
        tax_return_id,
        current_status
    FROM refund_statuses
    WHERE tax_return_id =
        '22222222-2222-2222-2222-222222222222'
),
history_insert AS (
    INSERT INTO refund_status_history (
        refund_status_history_id,
        tax_return_id,
        previous_status,
        new_status,
        source,
        changed_at
    )
    SELECT
        gen_random_uuid(),
        tax_return_id,
        current_status,
        'APPROVED',
        'PGADMIN_SIMULATION',
        NOW()
    FROM current_refund
    RETURNING tax_return_id
)
UPDATE refund_statuses
SET
    current_status = 'APPROVED',
    last_checked_at = NOW(),
    last_external_sync_at = NOW(),
    external_source = 'PGADMIN_SIMULATION'
WHERE tax_return_id IN (
    SELECT tax_return_id
    FROM history_insert
);

COMMIT;
```

---

## 9. Reset Demo Data

```sql
BEGIN;

DELETE FROM refund_status_history
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';

UPDATE tax_returns
SET
    external_refund_id =
        'IRS-DEMO-2025-0001'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';

UPDATE refund_statuses
SET
    current_status = 'PROCESSING',
    last_checked_at = NOW(),
    last_external_sync_at = NULL,
    external_source = 'IRS_SIMULATOR'
WHERE tax_return_id =
    '22222222-2222-2222-2222-222222222222';

COMMIT;
```

---

## 10. Data Consistency Checks

### Tax Returns Without Users

```sql
SELECT tr.*
FROM tax_returns tr
LEFT JOIN app_users u
    ON u.user_id = tr.user_id
WHERE u.user_id IS NULL;
```

Expected: no rows.

### Tax Returns Without Refund Status

```sql
SELECT tr.*
FROM tax_returns tr
LEFT JOIN refund_statuses rs
    ON rs.tax_return_id = tr.tax_return_id
WHERE rs.tax_return_id IS NULL;
```

Expected: no rows.

### Refund Status Without Tax Return

```sql
SELECT rs.*
FROM refund_statuses rs
LEFT JOIN tax_returns tr
    ON tr.tax_return_id = rs.tax_return_id
WHERE tr.tax_return_id IS NULL;
```

Expected: no rows.

### Duplicate External Refund IDs

```sql
SELECT
    external_refund_id,
    COUNT(*) AS occurrence_count
FROM tax_returns
WHERE external_refund_id IS NOT NULL
GROUP BY external_refund_id
HAVING COUNT(*) > 1;
```

### Records Never Synchronized

```sql
SELECT
    rs.*,
    tr.external_refund_id
FROM refund_statuses rs
JOIN tax_returns tr
    ON tr.tax_return_id = rs.tax_return_id
WHERE rs.last_external_sync_at IS NULL;
```

### Stale Records

```sql
SELECT
    u.display_name,
    tr.tax_return_id,
    rs.current_status,
    rs.last_external_sync_at,
    NOW() - rs.last_external_sync_at AS sync_age
FROM refund_statuses rs
JOIN tax_returns tr
    ON tr.tax_return_id = rs.tax_return_id
JOIN app_users u
    ON u.user_id = tr.user_id
WHERE rs.last_external_sync_at IS NULL
   OR rs.last_external_sync_at < NOW() - INTERVAL '1 hour'
ORDER BY rs.last_external_sync_at NULLS FIRST;
```

---

## 11. Flyway and Migration Investigation

### Flyway History

```sql
SELECT *
FROM flyway_schema_history
ORDER BY installed_rank;
```

If the table does not exist, the original schema may have been created by the
Kubernetes database bootstrap initContainer.

### Verify V2 Changes

```sql
SELECT
    EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'tax_returns'
          AND column_name = 'external_refund_id'
    ) AS has_external_refund_id,

    EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'refund_status_history'
    ) AS has_history_table;
```

---

## 12. Active Sessions and Locks

### Active Sessions

```sql
SELECT
    pid,
    usename,
    application_name,
    client_addr,
    state,
    query_start,
    query
FROM pg_stat_activity
WHERE datname = 'refund_platform'
ORDER BY query_start DESC;
```

### Long-Running Queries

```sql
SELECT
    pid,
    NOW() - query_start AS duration,
    state,
    query
FROM pg_stat_activity
WHERE datname = 'refund_platform'
  AND state <> 'idle'
  AND query_start < NOW() - INTERVAL '5 seconds'
ORDER BY duration DESC;
```

### Blocked and Blocking Queries

```sql
SELECT
    blocked.pid AS blocked_pid,
    blocked.query AS blocked_query,
    blocking.pid AS blocking_pid,
    blocking.query AS blocking_query
FROM pg_stat_activity blocked
JOIN pg_locks blocked_lock
    ON blocked_lock.pid = blocked.pid
JOIN pg_locks blocking_lock
    ON blocking_lock.locktype = blocked_lock.locktype
   AND blocking_lock.database IS NOT DISTINCT FROM blocked_lock.database
   AND blocking_lock.relation IS NOT DISTINCT FROM blocked_lock.relation
   AND blocking_lock.page IS NOT DISTINCT FROM blocked_lock.page
   AND blocking_lock.tuple IS NOT DISTINCT FROM blocked_lock.tuple
   AND blocking_lock.virtualxid IS NOT DISTINCT FROM blocked_lock.virtualxid
   AND blocking_lock.transactionid IS NOT DISTINCT FROM blocked_lock.transactionid
   AND blocking_lock.classid IS NOT DISTINCT FROM blocked_lock.classid
   AND blocking_lock.objid IS NOT DISTINCT FROM blocked_lock.objid
   AND blocking_lock.objsubid IS NOT DISTINCT FROM blocked_lock.objsubid
   AND blocking_lock.pid <> blocked_lock.pid
JOIN pg_stat_activity blocking
    ON blocking.pid = blocking_lock.pid
WHERE NOT blocked_lock.granted
  AND blocking_lock.granted;
```

---

## 13. Performance Investigation

### Table Sizes

```sql
SELECT
    table_name,
    pg_size_pretty(
        pg_total_relation_size(
            quote_ident(table_schema)
            || '.'
            || quote_ident(table_name)
        )
    ) AS total_size
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_type = 'BASE TABLE'
ORDER BY
    pg_total_relation_size(
        quote_ident(table_schema)
        || '.'
        || quote_ident(table_name)
    ) DESC;
```

### Row Estimates and Vacuum State

```sql
SELECT
    relname AS table_name,
    n_live_tup AS estimated_live_rows,
    n_dead_tup AS estimated_dead_rows,
    last_vacuum,
    last_autovacuum,
    last_analyze,
    last_autoanalyze
FROM pg_stat_user_tables
ORDER BY relname;
```

### Explain Latest Refund Query

```sql
EXPLAIN ANALYZE
SELECT
    u.display_name,
    tr.tax_return_id,
    tr.tax_year,
    tr.refund_amount,
    tr.filed_at,
    rs.current_status
FROM app_users u
JOIN tax_returns tr
    ON tr.user_id = u.user_id
JOIN refund_statuses rs
    ON rs.tax_return_id = tr.tax_return_id
WHERE u.external_identity_id =
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
ORDER BY tr.filed_at DESC
LIMIT 1;
```

---

## 14. Safe pgAdmin Workflow

Start a transaction:

```sql
BEGIN;
```

Run your changes, then inspect:

```sql
SELECT *
FROM refund_statuses;
```

Keep changes:

```sql
COMMIT;
```

Undo changes:

```sql
ROLLBACK;
```

For the cleanest end-to-end demo, update the external status through the IRS
Simulator API and then click **Refresh from IRS** in the UI. Use direct SQL
mainly for investigation, controlled simulations, and resetting demo data.
