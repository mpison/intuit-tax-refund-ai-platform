ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS email VARCHAR(255);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS first_name VARCHAR(100);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS last_name VARCHAR(100);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS phone_number VARCHAR(32);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ;

UPDATE app_users
SET
    email = COALESCE(
        email,
        LOWER(
            REPLACE(
                COALESCE(display_name, 'demo.user'),
                ' ',
                '.'
            )
        ) || '@demo.refund-platform.local'
    ),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, created_at, NOW());

ALTER TABLE app_users
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE app_users
    ALTER COLUMN created_at SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_app_users_external_identity_id
    ON app_users (external_identity_id);