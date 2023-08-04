CREATE TYPE dw.withdrawal_adjustment_status AS ENUM (
    'pending',
    'succeeded'
    );

CREATE TYPE dw.withdrawal_adjustment_type AS ENUM (
    'status_change',
    'domain_revision'
    );

CREATE TABLE dw.withdrawal_adjustment
(
    id                    bigserial                       NOT NULL,
    event_created_at      timestamp without time zone NOT NULL,
    event_occured_at      timestamp without time zone NOT NULL,
    sequence_id           bigint                          NOT NULL,
    adjustment_id         character varying               NOT NULL,
    new_domain_revision   bigint,
    new_withdrawal_status dw.withdrawal_status,
    party_revision        bigint                          NOT NULL,
    status                dw.withdrawal_adjustment_status NOT NULL,
    type                  dw.withdrawal_adjustment_type   NOT NULL,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    external_id           character varying,
    current               boolean DEFAULT true            NOT NULL,
    CONSTRAINT withdrawal_adjustment_pkey PRIMARY KEY (id),
    CONSTRAINT withdrawal_adjustment_uniq UNIQUE (adjustment_id, sequence_id)
);

CREATE INDEX withdrawal_adjustment_event_created_at_idx ON dw.withdrawal_adjustment USING btree (event_created_at);
CREATE INDEX withdrawal_adjustment_event_occured_at_idx ON dw.withdrawal_adjustment USING btree (event_occured_at);
CREATE INDEX withdrawal_adjustment_id_idx ON dw.withdrawal_adjustment USING btree (adjustment_id);



-- 3: required ChangesPlan         changes_plan
--      8: required base.Timestamp      operation_timestamp