# Keycloak Registration

In Keycloak Admin Console:

1. Select realm `refund-platform`.
2. Open Realm Settings.
3. Open Login.
4. Enable User registration.
5. Enable Forgot password if desired.
6. Save.

For `customer-ui`, verify:
- Public client
- Standard flow enabled
- Redirect URI: `http://localhost:3000/*`
- Web origin: `http://localhost:3000`
