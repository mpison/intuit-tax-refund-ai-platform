CREATE TABLE IF NOT EXISTS app_users (
    user_id UUID PRIMARY KEY,
    external_identity_id VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS tax_returns (
    tax_return_id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES app_users(user_id),
    tax_year INTEGER NOT NULL,
    filed_at TIMESTAMPTZ NOT NULL,
    refund_amount NUMERIC(12, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS refund_statuses (
    refund_status_id UUID PRIMARY KEY,
    tax_return_id UUID NOT NULL UNIQUE REFERENCES tax_returns(tax_return_id),
    current_status VARCHAR(40) NOT NULL,
    last_checked_at TIMESTAMPTZ NOT NULL
);
