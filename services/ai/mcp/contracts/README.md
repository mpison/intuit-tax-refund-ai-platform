# MCP Contracts

Both Java and Python implementations expose the same logical tools:

- `get_customer_by_identity`
- `get_latest_refund_by_identity`
- `get_refund_history_by_identity`
- `predict_refund_date`

v0.5.7.0 uses explicit identity or tax-return identifiers. JWT-derived
current-user resolution is planned for v0.5.7.1.
