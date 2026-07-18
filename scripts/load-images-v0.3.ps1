$ErrorActionPreference = "Stop"

kind load docker-image refund-status-service:0.3.0 --name refund-demo
kind load docker-image customer-ui:0.3.0 --name refund-demo
kind load docker-image refund-prediction-service:0.3.0 --name refund-demo
