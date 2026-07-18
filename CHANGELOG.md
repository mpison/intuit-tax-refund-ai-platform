# Changelog

## v0.2.0

### Added
- IRS Simulator service.
- Refund refresh use case.
- Refund status history persistence.
- External refund ID support.
- Demo IRS status update endpoint.
- UI refresh-from-IRS workflow.
- Kubernetes IRS deployment.
- Build/load PowerShell scripts.
- Migration and test documentation.

### Changed
- Backend image: `refund-status-service:0.2.0`.
- UI image: `customer-ui:0.2.0`.
- Refund service now calls IRS Simulator through Kubernetes DNS.
