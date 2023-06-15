CREATE TYPE dw.payment_session_result AS ENUM (
    'failed',
    'succeeded'
);

ALTER TABLE dw.payment_session_info
    ADD COLUMN IF NOT EXISTS payment_session_result dw.payment_session_result;