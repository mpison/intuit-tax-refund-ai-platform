# Release Notes v0.5.1

## Summary

Version 0.5.1 adds customer self-registration and profile management.

## Added

- Keycloak self-registration.
- First-login application-user synchronization.
- `GET /api/v1/users/me`.
- `PUT /api/v1/users/me`.
- Customer profile page.
- User profile fields in PostgreSQL.

## Security

- Identity comes from the authenticated JWT subject.
- Users can update only their own profile.
- No SSN or customer tax-document upload is included.


## Known limitations

• Newly registered users do not yet have tax returns.

• Refund Dashboard becomes available after
a tax return exists.

• Tax Return submission is planned for v0.5.2.