CREATE INDEX IF NOT EXISTS payment_status_info_event_created_at_idx ON dw.payment_status_info USING btree (event_created_at);
CREATE INDEX IF NOT EXISTS payment_currency_code_idx ON dw.payment USING btree (currency_code);