# Keycloak Setup Guide (v0.5.5)

## Realm
- refund-platform

## Clients
### customer-ui
- Public
- Redirect URI: http://localhost:3000/*
- Web Origin: http://localhost:3000

### admin-ui
- Public
- Redirect URI: http://localhost:3200/*
- Web Origin: http://localhost:3200

### Backend Resource Servers
- refund-status-service
- admin-service
- tax-policy-assistant-service
- refund-prediction-service

## Roles
- CUSTOMER
- ADMIN
- IRS_AGENT
- SYSTEM

## Spring Boot
issuer-uri:
http://localhost:8081/realms/refund-platform

## React
VITE_KEYCLOAK_URL=http://localhost:8081
VITE_KEYCLOAK_REALM=refund-platform
VITE_KEYCLOAK_CLIENT_ID=admin-ui
