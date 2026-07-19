ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS first_name VARCHAR(100);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS last_name VARCHAR(100);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS phone_number VARCHAR(32);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ;

UPDATE app_users
SET created_at = COALESCE(created_at, NOW());

UPDATE app_users
SET updated_at = COALESCE(updated_at, created_at, NOW());

ALTER TABLE app_users
    ALTER COLUMN created_at SET NOT NULL;