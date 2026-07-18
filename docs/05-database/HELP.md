# Database Help

Port-forward:
```powershell
kubectl port-forward -n refund-platform svc/postgres 5432:5432
```

pgAdmin:
```text
Host: localhost
Port: 5432
Database: refund_platform
Username: refund_user
Password: refund_password
```

Tables:
```sql
SELECT table_name
FROM information_schema.tables
WHERE table_schema='public'
ORDER BY table_name;
```

Vector count:
```sql
SELECT COUNT(*) FROM vector_store;
```
