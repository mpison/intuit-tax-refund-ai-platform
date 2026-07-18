ALTER TABLE tax_returns
    ADD COLUMN IF NOT EXISTS external_refund_id VARCHAR(100);

ALTER TABLE refund_statuses
    ADD COLUMN IF NOT EXISTS last_external_sync_at TIMESTAMPTZ;

ALTER TABLE refund_statuses
    ADD COLUMN IF NOT EXISTS external_source VARCHAR(40);

CREATE TABLE IF NOT EXISTS refund_status_history (
    refund_status_history_id UUID PRIMARY KEY,

    tax_return_id UUID
        NOT NULL
        REFERENCES tax_returns(tax_return_id),

    previous_status VARCHAR(40),

    new_status VARCHAR(40)
        NOT NULL,

    source VARCHAR(40)
        NOT NULL,

    changed_at TIMESTAMPTZ
        NOT NULL
);

CREATE INDEX IF NOT EXISTS
    idx_refund_status_history_tax_return_changed
ON refund_status_history (
    tax_return_id,
    changed_at DESC
);

UPDATE tax_returns
SET external_refund_id = 'IRS-DEMO-2025-0001'
WHERE tax_return_id = '22222222-2222-2222-2222-222222222222'
  AND external_refund_id IS NULL;

UPDATE refund_statuses
SET external_source = 'IRS_SIMULATOR'
WHERE tax_return_id = '22222222-2222-2222-2222-222222222222'
  AND external_source IS NULL;
