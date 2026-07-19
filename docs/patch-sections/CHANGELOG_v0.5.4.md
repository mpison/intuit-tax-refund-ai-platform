## v0.5.4

### Added

- Standalone IRS Simulator Console
- PostgreSQL-backed simulator records
- Simulator CRUD APIs
- `irs_refund_records` table
- Kubernetes deployment and schema Job
- Build, load, deploy, and port-forward scripts

### Changed

- Replaced the IRS simulator in-memory store with PostgreSQL persistence

### Security

- Console is local/demo-only
- Authentication is intentionally disabled
- Public ingress is unsupported

### Known Limitations

- Simulator records must match `tax_returns.external_refund_id`
- General administration is planned for v0.5.5
