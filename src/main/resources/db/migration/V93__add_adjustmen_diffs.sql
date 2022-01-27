ALTER TABLE nw.adjustment ADD COLUMN IF NOT EXISTS provider_amount_diff BIGINT DEFAULT 0;
ALTER TABLE nw.adjustment ADD COLUMN IF NOT EXISTS system_amount_diff BIGINT DEFAULT 0;
ALTER TABLE nw.adjustment ADD COLUMN IF NOT EXISTS external_income_amount_diff BIGINT DEFAULT 0;
ALTER TABLE nw.adjustment ADD COLUMN IF NOT EXISTS external_outcome_amount_diff BIGINT DEFAULT 0;