CREATE TABLE dw.exrate
(
    id                                 BIGSERIAL                   NOT NULL,
    event_id                           uuid UNIQUE                 NOT NULL,
    event_created_at                   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    source_currency_symbolic_code      CHARACTER VARYING           NOT NULL,
    source_currency_exponent           SMALLINT                    NOT NULL,
    destination_currency_symbolic_code CHARACTER VARYING           NOT NULL,
    destination_currency_exponent      SMALLINT                    NOT NULL,
    rational_p                         BIGINT                      NOT NULL,
    rational_q                         BIGINT                      NOT NULL,
    rate_timestamp                     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX rate_timestamp_idx ON dw.exrate (rate_timestamp);

CREATE INDEX source_currency_sc_destination_currency_sc_timestamp_idx ON dw.exrate (source_currency_symbolic_code,
                                                                                    destination_currency_symbolic_code,
                                                                                    rate_timestamp);
