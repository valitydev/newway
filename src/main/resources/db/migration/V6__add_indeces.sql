DROP INDEX IF EXISTS payment_status;
CREATE INDEX IF NOT EXISTS payment_status_info_idx ON dw.payment_status_info USING btree (status, event_created_at);