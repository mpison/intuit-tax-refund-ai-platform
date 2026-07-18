# Smoke test

1. Start the three port-forwards documented in README.
2. Open http://localhost:3000.
3. Log in as `mel.demo` / `Demo123!`.
4. Confirm the page shows `$2,850.00` and `Processing`.
5. Open http://localhost:8080/api/v1/refunds/latest without a token and confirm it returns 401.
