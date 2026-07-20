$ErrorActionPreference = "Stop"

kind load docker-image policy-management-service:0.5.6 --name refund-demo
kind load docker-image admin-ui:0.5.6 --name refund-demo
