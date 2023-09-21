DROP INDEX IF EXISTS withdrawal_adjustment_id_idx;
CREATE INDEX IF NOT EXISTS withdrawal_adjustment_idx ON dw.withdrawal_adjustment USING btree (adjustment_id, withdrawal_id);

ALTER TABLE dw.withdrawal_adjustment DROP CONSTRAINT IF EXISTS withdrawal_adjustment_uniq;
ALTER TABLE dw.withdrawal_adjustment
    ADD CONSTRAINT withdrawal_adjustment_uniq UNIQUE (adjustment_id, withdrawal_id, sequence_id);

