ALTER TABLE tax_returns
    ADD COLUMN IF NOT EXISTS filing_method VARCHAR(32);

UPDATE tax_returns
SET filing_method = COALESCE(
    filing_method,
    'E_FILE'
);

ALTER TABLE tax_returns
    ALTER COLUMN filing_method SET DEFAULT 'E_FILE';

CREATE UNIQUE INDEX IF NOT EXISTS ux_tax_returns_external_refund_id
    ON tax_returns (external_refund_id)
    WHERE external_refund_id IS NOT NULL;