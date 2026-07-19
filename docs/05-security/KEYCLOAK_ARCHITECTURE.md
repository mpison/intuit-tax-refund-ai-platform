# Keycloak Architecture

Customer UI
 -> Keycloak
 -> JWT
 -> refund-status-service

Admin UI
 -> Keycloak
 -> JWT (ADMIN)
 -> admin-service

JWTs are validated by backend services.
