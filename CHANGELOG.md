# Changelog

## v0.4.3
- Added blue financial-product theme.
- Updated hero, cards, timeline, prediction panel, and chatbot.
- Updated Customer UI image to `0.4.3`.
- No API or database changes.

## v0.4.2
- Standardized Java packages across services.
- Moved REST classes to `controller`.
- Moved business logic to `service`.
- Moved DTOs to `dto`.
- Moved domain/JPA classes to `model`.
- Moved repositories to `repository`.
- Moved integrations to `client`.
- Moved configuration to `config`.
- Structural refactor only.

## v0.4.1
- Added floating chatbot.
- Added chat history, clear action, auto-scroll, typing indicator, and responsive layout.

## v0.4.0
- Added Spring AI tax policy assistant.
- Added Ollama chat and embedding models.
- Added PGVector RAG.
- Added policy ingestion and source citations.

## v0.3.0
- Added Refund Prediction Service and prediction UI.

## v0.2.0
- Added IRS Simulator, refresh workflow, history, and timeline.

## v0.1.0
- Added Customer UI, Keycloak, Refund Status Service, PostgreSQL, and Kubernetes.


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
