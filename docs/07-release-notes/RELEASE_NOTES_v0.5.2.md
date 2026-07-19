# Release Notes v0.5.2

## Added
- Friendly no-return state.
- Simplified Add Filed Return form.
- Filed return creation API.
- Automatic FILED refund status.
- Auto-generated demo IRS reference.
- Flyway migrations and PostgreSQL support.

## Database
Existing databases are baselined at version 3. Flyway then applies versions 4 and 5 and creates `flyway_schema_history`.
