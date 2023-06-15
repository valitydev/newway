CREATE TYPE dw.payment_session_status AS ENUM (
    'started',
    'finished',
    'suspended',
    'activated'
);

CREATE TABLE dw.payment_session_info
(
    id                                bigserial                   NOT NULL,
    event_created_at                  timestamp without time zone NOT NULL,
    invoice_id                        character varying,
    payment_id                        character varying,
    sequence_id                       bigint,
    change_id                         integer,

    session_status                    dw.payment_session_status   NOT NULL,
    reason                            character varying,

    CONSTRAINT payment_session_pk PRIMARY KEY (id),
    CONSTRAINT payment_session_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);