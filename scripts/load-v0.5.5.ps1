$ErrorActionPreference = "Stop"

kind load docker-image admin-service:0.5.5 --name refund-demo
kind load docker-image admin-ui:0.5.5 --name refund-demo
