ALTER TABLE dw.payment_session_info
    ADD COLUMN IF NOT EXISTS payment_terminal integer;