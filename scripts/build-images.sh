#!/usr/bin/env bash
set -euo pipefail
docker build -t refund-status-service:0.1.0 services/core/refund-status-service
docker build -t customer-ui:0.1.0 applications/customer-ui
