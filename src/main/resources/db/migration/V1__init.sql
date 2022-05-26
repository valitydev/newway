CREATE SCHEMA IF NOT EXISTS dw;

CREATE TYPE dw.adjustment_cash_flow_type AS ENUM (
    'new_cash_flow',
    'old_cash_flow_inverse'
    );

CREATE TYPE dw.adjustment_status AS ENUM (
    'pending',
    'captured',
    'cancelled',
    'processed'
    );

CREATE TYPE dw.bank_card_payment_system AS ENUM (
    'visa',
    'mastercard',
    'visaelectron',
    'maestro',
    'forbrugsforeningen',
    'dankort',
    'amex',
    'dinersclub',
    'discover',
    'unionpay',
    'jcb',
    'nspkmir',
    'elo',
    'rupay',
    'ebt',
    'uzcard'
    );

CREATE TYPE dw.blocking AS ENUM (
    'unblocked',
    'blocked'
    );

CREATE TYPE dw.cash_flow_account AS ENUM (
    'merchant',
    'provider',
    'system',
    'external',
    'wallet'
    );

CREATE TYPE dw.challenge_resolution AS ENUM (
    'approved',
    'denied'
    );

CREATE TYPE dw.challenge_status AS ENUM (
    'pending',
    'cancelled',
    'completed',
    'failed'
    );

CREATE TYPE dw.chargeback_category AS ENUM (
    'fraud',
    'dispute',
    'authorisation',
    'processing_error'
    );

CREATE TYPE dw.chargeback_stage AS ENUM (
    'chargeback',
    'pre_arbitration',
    'arbitration'
    );

CREATE TYPE dw.chargeback_status AS ENUM (
    'pending',
    'accepted',
    'rejected',
    'cancelled'
    );

CREATE TYPE dw.contract_status AS ENUM (
    'active',
    'terminated',
    'expired'
    );

CREATE TYPE dw.contractor_type AS ENUM (
    'registered_user',
    'legal_entity',
    'private_entity'
    );

CREATE TYPE dw.deposit_adjustment_status AS ENUM (
    'pending',
    'succeeded'
    );

CREATE TYPE dw.deposit_revert_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE dw.deposit_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE dw.deposit_transfer_status AS ENUM (
    'created',
    'prepared',
    'committed',
    'cancelled'
    );

CREATE TYPE dw.destination_resource_type AS ENUM (
    'bank_card',
    'crypto_wallet',
    'digital_wallet',
    'generic'
    );

CREATE TYPE dw.destination_status AS ENUM (
    'authorized',
    'unauthorized'
    );

CREATE TYPE dw.fistful_cash_flow_change_type AS ENUM (
    'withdrawal',
    'deposit',
    'deposit_revert',
    'deposit_adjustment'
    );

CREATE TYPE dw.invoice_status AS ENUM (
    'unpaid',
    'paid',
    'cancelled',
    'fulfilled'
    );

CREATE TYPE dw.legal_entity AS ENUM (
    'russian_legal_entity',
    'international_legal_entity'
    );

CREATE TYPE dw.mobile_operator_type AS ENUM (
    'mts',
    'beeline',
    'megafone',
    'tele2',
    'yota'
    );

CREATE TYPE dw.payer_type AS ENUM (
    'payment_resource',
    'customer',
    'recurrent'
    );

CREATE TYPE dw.payment_change_type AS ENUM (
    'payment',
    'refund',
    'adjustment',
    'payout',
    'chargeback'
    );

CREATE TYPE dw.payment_flow_type AS ENUM (
    'instant',
    'hold'
    );

CREATE TYPE dw.payment_method_type AS ENUM (
    'bank_card',
    'payment_terminal',
    'digital_wallet',
    'tokenized_bank_card',
    'empty_cvv_bank_card',
    'crypto_currency',
    'mobile',
    'generic'
    );

CREATE TYPE dw.payment_status AS ENUM (
    'pending',
    'processed',
    'captured',
    'cancelled',
    'refunded',
    'failed',
    'charged_back'
    );

CREATE TYPE dw.payment_tool_type AS ENUM (
    'bank_card',
    'payment_terminal',
    'digital_wallet',
    'crypto_currency',
    'mobile_commerce',
    'crypto_currency_deprecated'
    );

CREATE TYPE dw.payout_account_type AS ENUM (
    'russian_payout_account',
    'international_payout_account'
    );

CREATE TYPE dw.payout_paid_status_details AS ENUM (
    'card_details',
    'account_details'
    );

CREATE TYPE dw.payout_status AS ENUM (
    'unpaid',
    'paid',
    'cancelled',
    'confirmed'
    );

CREATE TYPE dw.payout_tool_info AS ENUM (
    'russian_bank_account',
    'international_bank_account',
    'wallet_info',
    'payment_institution_account'
    );

CREATE TYPE dw.payout_type AS ENUM (
    'bank_card',
    'bank_account',
    'wallet'
    );

CREATE TYPE dw.private_entity AS ENUM (
    'russian_private_entity'
    );

CREATE TYPE dw.recurrent_payment_tool_status AS ENUM (
    'created',
    'acquired',
    'abandoned',
    'failed'
    );

CREATE TYPE dw.refund_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE dw.representative_document AS ENUM (
    'articles_of_association',
    'power_of_attorney',
    'expired'
    );

CREATE TYPE dw.risk_score AS ENUM (
    'low',
    'high',
    'fatal'
    );

CREATE TYPE dw.source_status AS ENUM (
    'authorized',
    'unauthorized'
    );

CREATE TYPE dw.suspension AS ENUM (
    'active',
    'suspended'
    );

CREATE TYPE dw.user_type AS ENUM (
    'internal_user',
    'external_user',
    'service_user'
    );

CREATE TYPE dw.withdrawal_session_status AS ENUM (
    'active',
    'success',
    'failed'
    );

CREATE TYPE dw.withdrawal_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE dw.withdrawal_transfer_status AS ENUM (
    'created',
    'prepared',
    'committed',
    'cancelled'
    );


-- TABLES
CREATE TABLE dw.adjustment
(
    id                           bigserial                                                        NOT NULL,
    event_created_at             timestamp without time zone                                      NOT NULL,
    domain_revision              bigint                                                           NOT NULL,
    adjustment_id                character varying                                                NOT NULL,
    payment_id                   character varying                                                NOT NULL,
    invoice_id                   character varying                                                NOT NULL,
    party_id                     character varying                                                NOT NULL,
    shop_id                      character varying                                                NOT NULL,
    created_at                   timestamp without time zone                                      NOT NULL,
    status                       dw.adjustment_status                                             NOT NULL,
    status_captured_at           timestamp without time zone,
    status_cancelled_at          timestamp without time zone,
    reason                       character varying                                                NOT NULL,
    wtime                        timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                      boolean                     DEFAULT true                         NOT NULL,
    party_revision               bigint,
    sequence_id                  bigint,
    change_id                    integer,
    payment_status               dw.payment_status,
    amount                       bigint                                                           NOT NULL,
    provider_amount_diff         bigint                      DEFAULT 0,
    system_amount_diff           bigint                      DEFAULT 0,
    external_income_amount_diff  bigint                      DEFAULT 0,
    external_outcome_amount_diff bigint                      DEFAULT 0,
    CONSTRAINT adjustment_pkey PRIMARY KEY (id),
    CONSTRAINT adjustment_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX adjustment_created_at ON dw.adjustment USING btree (created_at);
CREATE INDEX adjustment_event_created_at ON dw.adjustment USING btree (event_created_at);
CREATE INDEX adjustment_invoice_id ON dw.adjustment USING btree (invoice_id);
CREATE INDEX adjustment_party_id ON dw.adjustment USING btree (party_id);
CREATE INDEX adjustment_status ON dw.adjustment USING btree (status);


create table dw.cash_flow_link
(
    id               BIGSERIAL                   NOT NULL,
    event_created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    invoice_id       CHARACTER VARYING           NOT NULL,
    payment_id       CHARACTER VARYING           NOT NULL,
    sequence_id      BIGINT,
    change_id        INTEGER,
    wtime            TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    current          BOOLEAN                     NOT NULL DEFAULT false,
    CONSTRAINT cash_flow_link_pkey PRIMARY KEY (id),
    CONSTRAINT cash_flow_link_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);


CREATE TABLE dw.cash_flow
(
    id                             bigserial              NOT NULL,
    obj_id                         bigint                 NOT NULL,
    obj_type                       dw.payment_change_type NOT NULL,
    adj_flow_type                  dw.adjustment_cash_flow_type,
    source_account_type            dw.cash_flow_account   NOT NULL,
    source_account_type_value      character varying      NOT NULL,
    source_account_id              bigint                 NOT NULL,
    destination_account_type       dw.cash_flow_account   NOT NULL,
    destination_account_type_value character varying      NOT NULL,
    destination_account_id         bigint                 NOT NULL,
    amount                         bigint                 NOT NULL,
    currency_code                  character varying      NOT NULL,
    details                        character varying,
    CONSTRAINT cash_flow_pkey PRIMARY KEY (id)
);

CREATE INDEX cash_flow_idx ON dw.cash_flow USING btree (obj_id, obj_type);


CREATE TABLE dw.calendar
(
    id                bigserial                                                        NOT NULL,
    version_id        bigint                                                           NOT NULL,
    calendar_ref_id   integer                                                          NOT NULL,
    name              character varying                                                NOT NULL,
    description       character varying,
    timezone          character varying                                                NOT NULL,
    holidays_json     character varying                                                NOT NULL,
    first_day_of_week integer,
    wtime             timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current           boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT calendar_pkey PRIMARY KEY (id)
);

CREATE INDEX calendar_idx ON dw.calendar USING btree (calendar_ref_id);
CREATE INDEX calendar_version_id ON dw.calendar USING btree (version_id);


CREATE TABLE dw.category
(
    id              bigserial                                                        NOT NULL,
    version_id      bigint                                                           NOT NULL,
    category_ref_id integer                                                          NOT NULL,
    name            character varying                                                NOT NULL,
    description     character varying                                                NOT NULL,
    type            character varying,
    wtime           timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current         boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT category_pkey PRIMARY KEY (id)
);

CREATE INDEX category_idx ON dw.category USING btree (category_ref_id);
CREATE INDEX category_version_id ON dw.category USING btree (version_id);


CREATE TABLE dw.challenge
(
    id                    bigserial                                                        NOT NULL,
    event_created_at      timestamp without time zone                                      NOT NULL,
    event_occured_at      timestamp without time zone                                      NOT NULL,
    sequence_id           integer                                                          NOT NULL,
    identity_id           character varying                                                NOT NULL,
    challenge_id          character varying                                                NOT NULL,
    challenge_class_id    character varying                                                NOT NULL,
    challenge_status      dw.challenge_status                                              NOT NULL,
    challenge_resolution  dw.challenge_resolution,
    challenge_valid_until timestamp without time zone,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current               boolean                     DEFAULT true                         NOT NULL,
    proofs_json           character varying,
    CONSTRAINT challenge_pkey PRIMARY KEY (id),
    CONSTRAINT challenge_uniq UNIQUE (challenge_id, identity_id, sequence_id)
);

CREATE INDEX challenge_event_created_at_idx ON dw.challenge USING btree (event_created_at);
CREATE INDEX challenge_event_occured_at_idx ON dw.challenge USING btree (event_occured_at);
CREATE INDEX challenge_id_idx ON dw.challenge USING btree (challenge_id);


CREATE TABLE dw.chargeback
(
    id                 bigserial                                                        NOT NULL,
    sequence_id        bigint                                                           NOT NULL,
    change_id          integer                                                          NOT NULL,
    domain_revision    bigint                                                           NOT NULL,
    party_revision     bigint,
    chargeback_id      character varying                                                NOT NULL,
    payment_id         character varying                                                NOT NULL,
    invoice_id         character varying                                                NOT NULL,
    shop_id            character varying                                                NOT NULL,
    party_id           character varying                                                NOT NULL,
    external_id        character varying,
    event_created_at   timestamp without time zone                                      NOT NULL,
    created_at         timestamp without time zone                                      NOT NULL,
    status             dw.chargeback_status                                             NOT NULL,
    levy_amount        bigint,
    levy_currency_code character varying,
    amount             bigint,
    currency_code      character varying,
    reason_code        character varying,
    reason_category    dw.chargeback_category                                           NOT NULL,
    stage              dw.chargeback_stage                                              NOT NULL,
    current            boolean                     DEFAULT true                         NOT NULL,
    context            bytea,
    wtime              timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT chargeback_pkey PRIMARY KEY (id),
    CONSTRAINT chargeback_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX chargeback_created_at ON dw.chargeback USING btree (created_at);
CREATE INDEX chargeback_event_created_at ON dw.chargeback USING btree (event_created_at);
CREATE INDEX chargeback_invoice_id ON dw.chargeback USING btree (invoice_id);
CREATE INDEX chargeback_party_id ON dw.chargeback USING btree (party_id);
CREATE INDEX chargeback_status ON dw.chargeback USING btree (status);


CREATE TABLE dw.contract
(
    id                                                         bigserial                                                        NOT NULL,
    event_created_at                                           timestamp without time zone                                      NOT NULL,
    contract_id                                                character varying                                                NOT NULL,
    party_id                                                   character varying                                                NOT NULL,
    payment_institution_id                                     integer,
    created_at                                                 timestamp without time zone                                      NOT NULL,
    valid_since                                                timestamp without time zone,
    valid_until                                                timestamp without time zone,
    status                                                     dw.contract_status                                               NOT NULL,
    status_terminated_at                                       timestamp without time zone,
    terms_id                                                   integer                                                          NOT NULL,
    legal_agreement_signed_at                                  timestamp without time zone,
    legal_agreement_id                                         character varying,
    legal_agreement_valid_until                                timestamp without time zone,
    report_act_schedule_id                                     integer,
    report_act_signer_position                                 character varying,
    report_act_signer_full_name                                character varying,
    report_act_signer_document                                 dw.representative_document,
    report_act_signer_doc_power_of_attorney_signed_at          timestamp without time zone,
    report_act_signer_doc_power_of_attorney_legal_agreement_id character varying,
    report_act_signer_doc_power_of_attorney_valid_until        timestamp without time zone,
    contractor_id                                              character varying,
    wtime                                                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                                    boolean                     DEFAULT true                         NOT NULL,
    sequence_id                                                integer,
    change_id                                                  integer,
    claim_effect_id                                            integer,
    CONSTRAINT contract_pkey PRIMARY KEY (id),
    CONSTRAINT contract_uniq UNIQUE (party_id, contract_id, sequence_id, change_id, claim_effect_id)
);

CREATE INDEX contract_contract_id ON dw.contract USING btree (contract_id);
CREATE INDEX contract_created_at ON dw.contract USING btree (created_at);
CREATE INDEX contract_event_created_at ON dw.contract USING btree (event_created_at);
CREATE INDEX contract_party_id ON dw.contract USING btree (party_id);


CREATE TABLE dw.contract_adjustment
(
    id                     bigserial                   NOT NULL,
    cntrct_id              bigint                      NOT NULL,
    contract_adjustment_id character varying           NOT NULL,
    created_at             timestamp without time zone NOT NULL,
    valid_since            timestamp without time zone,
    valid_until            timestamp without time zone,
    terms_id               integer                     NOT NULL,
    CONSTRAINT contract_adjustment_pkey PRIMARY KEY (id)
);

CREATE INDEX contract_adjustment_idx ON dw.contract_adjustment USING btree (cntrct_id);


CREATE TABLE dw.contract_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT contract_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX contract_revision_idx ON dw.contract_revision USING btree (obj_id, revision);


CREATE TABLE dw.contractor
(
    id                                             bigserial                                                        NOT NULL,
    event_created_at                               timestamp without time zone                                      NOT NULL,
    party_id                                       character varying                                                NOT NULL,
    contractor_id                                  character varying                                                NOT NULL,
    type                                           dw.contractor_type                                               NOT NULL,
    identificational_level                         character varying,
    registered_user_email                          character varying,
    legal_entity                                   dw.legal_entity,
    russian_legal_entity_registered_name           character varying,
    russian_legal_entity_registered_number         character varying,
    russian_legal_entity_inn                       character varying,
    russian_legal_entity_actual_address            character varying,
    russian_legal_entity_post_address              character varying,
    russian_legal_entity_representative_position   character varying,
    russian_legal_entity_representative_full_name  character varying,
    russian_legal_entity_representative_document   character varying,
    russian_legal_entity_russian_bank_account      character varying,
    russian_legal_entity_russian_bank_name         character varying,
    russian_legal_entity_russian_bank_post_account character varying,
    russian_legal_entity_russian_bank_bik          character varying,
    international_legal_entity_legal_name          character varying,
    international_legal_entity_trading_name        character varying,
    international_legal_entity_registered_address  character varying,
    international_legal_entity_actual_address      character varying,
    international_legal_entity_registered_number   character varying,
    private_entity                                 dw.private_entity,
    russian_private_entity_first_name              character varying,
    russian_private_entity_second_name             character varying,
    russian_private_entity_middle_name             character varying,
    russian_private_entity_phone_number            character varying,
    russian_private_entity_email                   character varying,
    wtime                                          timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                        boolean                     DEFAULT true                         NOT NULL,
    sequence_id                                    integer,
    change_id                                      integer,
    claim_effect_id                                integer,
    international_legal_entity_country_code        character varying,
    CONSTRAINT contractor_pkey PRIMARY KEY (id),
    CONSTRAINT contractor_uniq UNIQUE (party_id, contractor_id, sequence_id, change_id, claim_effect_id)
);

CREATE INDEX contractor_contractor_id ON dw.contractor USING btree (contractor_id);
CREATE INDEX contractor_event_created_at ON dw.contractor USING btree (event_created_at);
CREATE INDEX contractor_party_id ON dw.contractor USING btree (party_id);


CREATE TABLE dw.contractor_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT contractor_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX contractor_revision_idx ON dw.contractor_revision USING btree (obj_id, revision);


CREATE TABLE dw.country
(
    id             bigserial                                                        NOT NULL,
    version_id     bigint                                                           NOT NULL,
    country_ref_id character varying                                                NOT NULL,
    name           character varying                                                NOT NULL,
    trade_bloc     text[]                                                           NOT NULL,
    wtime          timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current        boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT country_pkey PRIMARY KEY (id)
);



CREATE TABLE dw.currency
(
    id              bigserial                                                        NOT NULL,
    version_id      bigint                                                           NOT NULL,
    currency_ref_id character varying                                                NOT NULL,
    name            character varying                                                NOT NULL,
    symbolic_code   character varying                                                NOT NULL,
    numeric_code    smallint                                                         NOT NULL,
    exponent        smallint                                                         NOT NULL,
    wtime           timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current         boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT currency_pkey PRIMARY KEY (id)
);

CREATE INDEX currency_idx ON dw.currency USING btree (currency_ref_id);
CREATE INDEX currency_version_id ON dw.currency USING btree (version_id);


CREATE TABLE dw.deposit
(
    id                      bigserial                                                        NOT NULL,
    event_created_at        timestamp without time zone                                      NOT NULL,
    event_occured_at        timestamp without time zone                                      NOT NULL,
    sequence_id             integer                                                          NOT NULL,
    source_id               character varying                                                NOT NULL,
    wallet_id               character varying                                                NOT NULL,
    deposit_id              character varying                                                NOT NULL,
    amount                  bigint                                                           NOT NULL,
    fee                     bigint,
    provider_fee            bigint,
    currency_code           character varying                                                NOT NULL,
    deposit_status          dw.deposit_status                                                NOT NULL,
    deposit_transfer_status dw.deposit_transfer_status,
    wtime                   timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                 boolean                     DEFAULT true                         NOT NULL,
    external_id             character varying,
    CONSTRAINT deposit_pkey PRIMARY KEY (id),
    CONSTRAINT deposit_uniq UNIQUE (deposit_id, sequence_id)
);

CREATE INDEX deposit_event_created_at_idx ON dw.deposit USING btree (event_created_at);
CREATE INDEX deposit_event_occured_at_idx ON dw.deposit USING btree (event_occured_at);
CREATE INDEX deposit_id_idx ON dw.deposit USING btree (deposit_id);


CREATE TABLE dw.deposit_adjustment
(
    id               bigserial                                                        NOT NULL,
    event_created_at timestamp without time zone                                      NOT NULL,
    event_occured_at timestamp without time zone                                      NOT NULL,
    sequence_id      integer                                                          NOT NULL,
    source_id        character varying                                                NOT NULL,
    wallet_id        character varying                                                NOT NULL,
    deposit_id       character varying                                                NOT NULL,
    adjustment_id    character varying                                                NOT NULL,
    amount           bigint,
    fee              bigint,
    provider_fee     bigint,
    currency_code    character varying,
    status           dw.deposit_adjustment_status                                     NOT NULL,
    transfer_status  dw.deposit_transfer_status,
    deposit_status   dw.deposit_status,
    external_id      character varying,
    wtime            timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current          boolean                     DEFAULT true                         NOT NULL,
    party_revision   bigint                      DEFAULT 0                            NOT NULL,
    domain_revision  bigint                      DEFAULT 0                            NOT NULL,
    CONSTRAINT deposit_adjustment_pkey PRIMARY KEY (id),
    CONSTRAINT deposit_adjustment_uniq UNIQUE (deposit_id, adjustment_id, sequence_id)
);

CREATE INDEX deposit_adjustment_event_created_at_idx ON dw.deposit_adjustment USING btree (event_created_at);
CREATE INDEX deposit_adjustment_id_idx ON dw.deposit_adjustment USING btree (deposit_id);


CREATE TABLE dw.deposit_revert
(
    id               bigserial                                                        NOT NULL,
    event_created_at timestamp without time zone                                      NOT NULL,
    event_occured_at timestamp without time zone                                      NOT NULL,
    sequence_id      integer                                                          NOT NULL,
    source_id        character varying                                                NOT NULL,
    wallet_id        character varying                                                NOT NULL,
    deposit_id       character varying                                                NOT NULL,
    revert_id        character varying                                                NOT NULL,
    amount           bigint                                                           NOT NULL,
    fee              bigint,
    provider_fee     bigint,
    currency_code    character varying                                                NOT NULL,
    status           dw.deposit_revert_status                                         NOT NULL,
    transfer_status  dw.deposit_transfer_status,
    reason           character varying,
    external_id      character varying,
    wtime            timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current          boolean                     DEFAULT true                         NOT NULL,
    party_revision   bigint                      DEFAULT 0                            NOT NULL,
    domain_revision  bigint                      DEFAULT 0                            NOT NULL,
    CONSTRAINT deposit_revert_pkey PRIMARY KEY (id),
    CONSTRAINT deposit_revert_uniq UNIQUE (deposit_id, revert_id, sequence_id)
);

CREATE INDEX deposit_revert_event_created_at_idx ON dw.deposit_revert USING btree (event_created_at);
CREATE INDEX deposit_revert_id_idx ON dw.deposit_revert USING btree (deposit_id);


CREATE TABLE dw.destination
(
    id                                bigserial                                                        NOT NULL,
    event_created_at                  timestamp without time zone                                      NOT NULL,
    event_occured_at                  timestamp without time zone                                      NOT NULL,
    sequence_id                       integer                                                          NOT NULL,
    destination_id                    character varying                                                NOT NULL,
    destination_name                  character varying                                                NOT NULL,
    destination_status                dw.destination_status                                            NOT NULL,
    resource_bank_card_token          character varying,
    resource_bank_card_payment_system character varying,
    resource_bank_card_bin            character varying,
    resource_bank_card_masked_pan     character varying,
    account_id                        character varying,
    identity_id                       character varying,
    party_id                          character varying,
    accounter_account_id              bigint,
    currency_code                     character varying,
    wtime                             timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                           boolean                     DEFAULT true                         NOT NULL,
    external_id                       character varying,
    created_at                        timestamp without time zone,
    context_json                      character varying,
    resource_crypto_wallet_id         character varying,
    resource_crypto_wallet_type       character varying,
    resource_type                     dw.destination_resource_type                                     NOT NULL,
    resource_crypto_wallet_data       character varying,
    resource_bank_card_type           character varying,
    resource_bank_card_issuer_country character varying,
    resource_bank_card_bank_name      character varying,
    resource_digital_wallet_id        character varying,
    resource_digital_wallet_data      character varying,
    CONSTRAINT destination_pkey PRIMARY KEY (id),
    CONSTRAINT destination_uniq UNIQUE (destination_id, sequence_id)
);

CREATE INDEX destination_event_created_at_idx ON dw.destination USING btree (event_created_at);
CREATE INDEX destination_event_occured_at_idx ON dw.destination USING btree (event_occured_at);
CREATE INDEX destination_id_idx ON dw.destination USING btree (destination_id);


CREATE TABLE dw.fistful_cash_flow
(
    id                             bigserial                        NOT NULL,
    obj_id                         bigint                           NOT NULL,
    source_account_type            dw.cash_flow_account             NOT NULL,
    source_account_type_value      character varying                NOT NULL,
    source_account_id              character varying                NOT NULL,
    destination_account_type       dw.cash_flow_account             NOT NULL,
    destination_account_type_value character varying                NOT NULL,
    destination_account_id         character varying                NOT NULL,
    amount                         bigint                           NOT NULL,
    currency_code                  character varying                NOT NULL,
    details                        character varying,
    obj_type                       dw.fistful_cash_flow_change_type NOT NULL,
    CONSTRAINT fistful_cash_flow_pkey PRIMARY KEY (id)
);

CREATE INDEX fistful_cash_flow_obj_id_idx ON dw.fistful_cash_flow USING btree (obj_id);


CREATE TABLE dw.identity
(
    id                             bigserial                                                        NOT NULL,
    event_created_at               timestamp without time zone                                      NOT NULL,
    event_occured_at               timestamp without time zone                                      NOT NULL,
    sequence_id                    integer                                                          NOT NULL,
    party_id                       character varying                                                NOT NULL,
    party_contract_id              character varying,
    identity_id                    character varying                                                NOT NULL,
    identity_provider_id           character varying                                                NOT NULL,
    identity_effective_chalenge_id character varying,
    identity_level_id              character varying,
    wtime                          timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                        boolean                     DEFAULT true                         NOT NULL,
    external_id                    character varying,
    blocked                        boolean,
    context_json                   character varying,
    CONSTRAINT identity_pkey PRIMARY KEY (id),
    CONSTRAINT identity_uniq UNIQUE (identity_id, sequence_id)
);

CREATE INDEX identity_event_created_at_idx ON dw.identity USING btree (event_created_at);
CREATE INDEX identity_event_occured_at_idx ON dw.identity USING btree (event_occured_at);
CREATE INDEX identity_id_idx ON dw.identity USING btree (identity_id);
CREATE INDEX identity_party_id_idx ON dw.identity USING btree (party_id);


CREATE TABLE dw.inspector
(
    id                    bigserial                                                        NOT NULL,
    version_id            bigint                                                           NOT NULL,
    inspector_ref_id      integer                                                          NOT NULL,
    name                  character varying                                                NOT NULL,
    description           character varying                                                NOT NULL,
    proxy_ref_id          integer                                                          NOT NULL,
    proxy_additional_json character varying                                                NOT NULL,
    fallback_risk_score   character varying,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current               boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT inspector_pkey PRIMARY KEY (id)
);

CREATE INDEX inspector_idx ON dw.inspector USING btree (inspector_ref_id);
CREATE INDEX inspector_version_id ON dw.inspector USING btree (version_id);


CREATE TABLE IF NOT EXISTS dw.invoice
(
    id                  bigserial                                      NOT NULL,
    event_created_at    timestamp without time zone                    NOT NULL,
    invoice_id          character varying COLLATE pg_catalog."default" NOT NULL,
    party_id            character varying COLLATE pg_catalog."default" NOT NULL,
    shop_id             character varying COLLATE pg_catalog."default" NOT NULL,
    party_revision      bigint,
    created_at          timestamp without time zone                    NOT NULL,

    details_product     character varying COLLATE pg_catalog."default" NOT NULL,
    details_description character varying COLLATE pg_catalog."default",
    due                 timestamp without time zone                    NOT NULL,
    amount              bigint                                         NOT NULL,
    currency_code       character varying COLLATE pg_catalog."default" NOT NULL,
    context             bytea,
    template_id         character varying COLLATE pg_catalog."default",
    wtime               timestamp without time zone                    NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id         bigint,
    change_id           integer,
    external_id         character varying COLLATE pg_catalog."default",
    CONSTRAINT invoice_pkey PRIMARY KEY (id),
    CONSTRAINT invoice_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

-- TODO refactor indices
CREATE INDEX invoice_created_at ON dw.invoice USING btree (created_at);
CREATE INDEX invoice_event_created_at ON dw.invoice USING btree (event_created_at);
CREATE INDEX invoice_external_id_idx ON dw.invoice USING btree (external_id) WHERE (external_id IS NOT NULL);
CREATE INDEX invoice_invoice_id ON dw.invoice USING btree (invoice_id);
CREATE INDEX invoice_party_id ON dw.invoice USING btree (party_id);

CREATE TABLE IF NOT EXISTS dw.invoice_status_info
(
    id               BIGSERIAL,
    event_created_at timestamp without time zone                    NOT NULL,
    invoice_id       character varying COLLATE pg_catalog."default" NOT NULL,
    status           dw.invoice_status                              NOT NULL,
    details          character varying COLLATE pg_catalog."default",
    wtime            timestamp without time zone                    NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    current          boolean                                        DEFAULT false NOT NULL,
    sequence_id      bigint,
    change_id        integer,
    external_id      character varying COLLATE pg_catalog."default",
    CONSTRAINT invoice_status_pkey PRIMARY KEY (id),
    CONSTRAINT invoice_status_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX invoice_status ON dw.invoice_status_info USING btree (status);


CREATE TABLE IF NOT EXISTS dw.invoice_cart
(
    id                  bigserial                                      NOT NULL,
    event_created_at    timestamp without time zone                    NOT NULL,
    invoice_id          character varying COLLATE pg_catalog."default" NOT NULL,
    product             character varying COLLATE pg_catalog."default",
    quantity            integer                                        NOT NULL,
    amount              bigint                                         NOT NULL,
    currency_code       character varying COLLATE pg_catalog."default" NOT NULL,
    metadata_json       character varying COLLATE pg_catalog."default" NOT NULL,
    wtime               timestamp without time zone                    NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id         bigint,
    change_id           integer,
    CONSTRAINT invoice_cart_pkey PRIMARY KEY (id)
);

CREATE INDEX invoice_cart_invoice_id ON dw.invoice_cart USING btree (invoice_id);


CREATE TABLE dw.party
(
    id                         bigserial                                                        NOT NULL,
    event_created_at           timestamp without time zone                                      NOT NULL,
    party_id                   character varying                                                NOT NULL,
    contact_info_email         character varying                                                NOT NULL,
    created_at                 timestamp without time zone                                      NOT NULL,
    blocking                   dw.blocking                                                      NOT NULL,
    blocking_unblocked_reason  character varying,
    blocking_unblocked_since   timestamp without time zone,
    blocking_blocked_reason    character varying,
    blocking_blocked_since     timestamp without time zone,
    suspension                 dw.suspension                                                    NOT NULL,
    suspension_active_since    timestamp without time zone,
    suspension_suspended_since timestamp without time zone,
    revision                   bigint                                                           NOT NULL,
    revision_changed_at        timestamp without time zone,
    party_meta_set_ns          character varying,
    party_meta_set_data_json   character varying,
    wtime                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                    boolean                     DEFAULT true                         NOT NULL,
    sequence_id                integer,
    change_id                  integer,
    CONSTRAINT party_pkey PRIMARY KEY (id),
    CONSTRAINT party_uniq UNIQUE (party_id, sequence_id, change_id)
);

CREATE INDEX party_contact_info_email ON dw.party USING btree (contact_info_email);
CREATE INDEX party_created_at ON dw.party USING btree (created_at);
CREATE INDEX party_current ON dw.party USING btree (current);
CREATE INDEX party_event_created_at ON dw.party USING btree (event_created_at);
CREATE INDEX party_party_id ON dw.party USING btree (party_id);

CREATE TABLE IF NOT EXISTS dw.payment
(
    id                              BIGSERIAL                   NOT NULL,
    event_created_at                TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    invoice_id                      CHARACTER VARYING           NOT NULL,
    payment_id                      CHARACTER VARYING           NOT NULL,
    created_at                      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    party_id                        CHARACTER VARYING           NOT NULL,
    shop_id                         CHARACTER VARYING           NOT NULL,
    domain_revision                 BIGINT                      NOT NULL,
    party_revision                  BIGINT,
    amount                          BIGINT                      NOT NULL,
    currency_code                   CHARACTER VARYING           NOT NULL,
    make_recurrent                  BOOLEAN,
    sequence_id                     BIGINT,
    change_id                       INTEGER,
    wtime                           TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    external_id                     character varying COLLATE pg_catalog."default",
    payment_flow_type               dw.payment_flow_type        NOT NULL,
    payment_flow_on_hold_expiration character varying COLLATE pg_catalog."default",
    payment_flow_held_until         timestamp without time zone,

    CONSTRAINT payment_pkey PRIMARY KEY (id),
    CONSTRAINT payment_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);

-- TODO refactor indices
CREATE INDEX payment_created_at ON dw.payment USING btree (created_at);
CREATE INDEX payment_event_created_at ON dw.payment USING btree (event_created_at);
CREATE INDEX payment_external_id_idx ON dw.payment USING btree (external_id) WHERE (external_id IS NOT NULL);
CREATE INDEX payment_invoice_id ON dw.payment USING btree (invoice_id);
CREATE INDEX payment_party_id ON dw.payment USING btree (party_id);

CREATE TABLE IF NOT EXISTS dw.payment_fee
(
    id                               bigserial                   NOT NULL,
    event_created_at                 timestamp without time zone NOT NULL,
    invoice_id                       character varying           NOT NULL,
    payment_id                       character varying           NOT NULL,
    fee                              BIGINT,
    provider_fee                     BIGINT,
    external_fee                     BIGINT,
    guarantee_deposit                BIGINT,
    current                          BOOLEAN                     NOT NULL DEFAULT false,
    wtime                            timestamp without time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id                      bigint,
    change_id                        integer,
    CONSTRAINT payment_fee_pkey PRIMARY KEY (id),
    CONSTRAINT payment_fee_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);


CREATE TABLE IF NOT EXISTS dw.payment_route
(
    id                              BIGSERIAL                   NOT NULL,
    event_created_at                TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    invoice_id                      CHARACTER VARYING           NOT NULL,
    payment_id                      CHARACTER VARYING           NOT NULL,
    route_provider_id               INTEGER,
    route_terminal_id               INTEGER,
    sequence_id                     BIGINT,
    change_id                       INTEGER,
    wtime                           TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    current                         BOOLEAN                     NOT NULL DEFAULT false,

    CONSTRAINT payment_route_pkey PRIMARY KEY (id),
    CONSTRAINT payment_route_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);


CREATE TABLE IF NOT EXISTS dw.payment_status_info
(
    id                               bigserial                   NOT NULL,
    event_created_at                 timestamp without time zone NOT NULL,
    invoice_id                       character varying           NOT NULL,
    payment_id                       character varying           NOT NULL,
    status                           dw.payment_status           NOT NULL,
    reason                           character varying,
    amount                           BIGINT,
    currency_code                    CHARACTER VARYING,
    cart_json                        CHARACTER VARYING,
    current                          BOOLEAN                     NOT NULL DEFAULT false,
    wtime                            timestamp without time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id                      bigint,
    change_id                        integer,
    CONSTRAINT payment_status_pkey PRIMARY KEY (id),
    CONSTRAINT payment_status_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);

CREATE INDEX payment_status ON dw.payment_status_info USING btree (status);


CREATE TABLE IF NOT EXISTS dw.payment_payer_info
(
    id                                bigserial                   NOT NULL,
    event_created_at                  timestamp without time zone NOT NULL,
    invoice_id                        character varying,
    payment_id                        character varying,
    payer_type                        dw.payer_type               NOT NULL,
    payment_tool_type                 dw.payment_tool_type        NOT NULL,
    bank_card_token                   character varying,
    bank_card_payment_system          character varying,
    bank_card_bin                     character varying,
    bank_card_masked_pan              character varying,
    bank_card_token_provider          character varying,
    payment_terminal_type             character varying,
    digital_wallet_provider           character varying,
    digital_wallet_id                 character varying,
    payment_session_id                character varying,
    ip_address                        character varying,
    fingerprint                       character varying,
    phone_number                      character varying,
    email                             character varying,
    customer_id                       character varying,
    customer_binding_id               character varying,
    customer_rec_payment_tool_id      character varying,

    recurrent_parent_invoice_id       character varying,
    recurrent_parent_payment_id       character varying,

    crypto_currency_type              character varying,
    mobile_phone_cc                   character varying,
    mobile_phone_ctn                  character varying,
    issuer_country                    character varying,
    bank_name                         character varying,
    bank_card_cardholder_name         character varying,
    mobile_operator                   character varying,

    wtime                             timestamp without time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id                       bigint,
    change_id                         integer,
    CONSTRAINT payment_payment_payer_info_pkey PRIMARY KEY (id),
    CONSTRAINT payment_payment_payer_info_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);


CREATE TABLE IF NOT EXISTS dw.payment_additional_info
(
    id                    bigserial                   NOT NULL,
    event_created_at      timestamp without time zone NOT NULL,
    invoice_id            character varying           NOT NULL,
    payment_id            character varying           NOT NULL,
    transaction_id        character varying,
    extra_json            character varying,
    rrn                   character varying,
    approval_code         character varying,
    acs_url               character varying,
    md                    character varying,
    term_url              character varying,
    eci                   character varying,
    cavv                  character varying,
    xid                   character varying,
    cavv_algorithm        character varying,
    three_ds_verification character varying,
    current               boolean                     NOT NULL DEFAULT false,
    wtime                 timestamp without time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id           bigint,
    change_id             integer,
    CONSTRAINT payment_additional_info_pkey PRIMARY KEY (id),
    CONSTRAINT payment_additional_info_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);


CREATE TABLE IF NOT EXISTS dw.payment_recurrent_info
(
    id                                bigserial                   NOT NULL,
    event_created_at                  timestamp without time zone NOT NULL,
    invoice_id                        character varying           NOT NULL,
    payment_id                        character varying           NOT NULL,

    token                             character varying,
    current                           BOOLEAN                     NOT NULL DEFAULT false,
    wtime                             timestamp without time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id                       bigint,
    change_id                         integer,
    CONSTRAINT payment_recurrent_info_pkey PRIMARY KEY (id),
    CONSTRAINT payment_recurrent_info_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);

CREATE TABLE IF NOT EXISTS dw.payment_risk_data
(
    id               bigserial                   NOT NULL,
    event_created_at timestamp without time zone NOT NULL,
    invoice_id       character varying           NOT NULL,
    payment_id       character varying           NOT NULL,
    risk_score       dw.risk_score               NOT NULL,
    current          BOOLEAN                     NOT NULL DEFAULT false,
    wtime            timestamp without time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    sequence_id      bigint,
    change_id        integer,
    CONSTRAINT payment_risk_data_pkey PRIMARY KEY (id),
    CONSTRAINT payment_risk_data_uniq UNIQUE (invoice_id, payment_id, sequence_id, change_id)
);


CREATE TABLE dw.payment_institution
(
    id                                    bigserial                                                        NOT NULL,
    version_id                            bigint                                                           NOT NULL,
    payment_institution_ref_id            integer                                                          NOT NULL,
    name                                  character varying                                                NOT NULL,
    description                           character varying,
    calendar_ref_id                       integer,
    system_account_set_json               character varying                                                NOT NULL,
    default_contract_template_json        character varying                                                NOT NULL,
    default_wallet_contract_template_json character varying,
    providers_json                        character varying,
    inspector_json                        character varying                                                NOT NULL,
    realm                                 character varying                                                NOT NULL,
    residences_json                       character varying                                                NOT NULL,
    wtime                                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                               boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT payment_institution_pkey PRIMARY KEY (id)
);

CREATE INDEX payment_institution_idx ON dw.payment_institution USING btree (payment_institution_ref_id);
CREATE INDEX payment_institution_version_id ON dw.payment_institution USING btree (version_id);


CREATE TABLE dw.payment_method
(
    id                    bigserial                                                        NOT NULL,
    version_id            bigint                                                           NOT NULL,
    payment_method_ref_id character varying                                                NOT NULL,
    name                  character varying                                                NOT NULL,
    description           character varying                                                NOT NULL,
    type                  dw.payment_method_type                                           NOT NULL,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current               boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT payment_method_pkey PRIMARY KEY (id)
);

CREATE INDEX payment_method_idx ON dw.payment_method USING btree (payment_method_ref_id);
CREATE INDEX payment_method_version_id ON dw.payment_method USING btree (version_id);


CREATE TABLE dw.payment_routing_rule
(
    id                     bigserial                                                        NOT NULL,
    rule_ref_id            integer                                                          NOT NULL,
    name                   character varying                                                NOT NULL,
    description            character varying,
    wtime                  timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                boolean                     DEFAULT true                         NOT NULL,
    routing_decisions_json character varying                                                NOT NULL,
    version_id             bigint                                                           NOT NULL,
    CONSTRAINT payment_routing_rule_pkey PRIMARY KEY (id)
);

CREATE INDEX payment_routing_rule_ref_id ON dw.payment_routing_rule USING btree (rule_ref_id);

CREATE TABLE dw.payout
(
    id                bigserial                                                        NOT NULL,
    payout_id         character varying                                                NOT NULL,
    event_created_at  timestamp without time zone                                      NOT NULL,
    sequence_id       integer                                                          NOT NULL,
    created_at        timestamp without time zone                                      NOT NULL,
    party_id          character varying                                                NOT NULL,
    shop_id           character varying                                                NOT NULL,
    status            dw.payout_status                                                 NOT NULL,
    payout_tool_id    character varying                                                NOT NULL,
    amount            bigint                                                           NOT NULL,
    fee               bigint                      DEFAULT 0                            NOT NULL,
    currency_code     character varying                                                NOT NULL,
    cancelled_details character varying,
    wtime             timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current           boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT payout_id_pkey PRIMARY KEY (id),
    CONSTRAINT payout_payout_id_ukey UNIQUE (payout_id, sequence_id)
);

CREATE TABLE dw.payout_method
(
    id                   bigserial                                                        NOT NULL,
    version_id           bigint                                                           NOT NULL,
    payout_method_ref_id character varying                                                NOT NULL,
    name                 character varying                                                NOT NULL,
    description          character varying                                                NOT NULL,
    wtime                timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current              boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT payout_method_pkey PRIMARY KEY (id)
);

CREATE INDEX payout_method_idx ON dw.payout_method USING btree (payout_method_ref_id);
CREATE INDEX payout_method_version_id ON dw.payout_method USING btree (version_id);


CREATE TABLE dw.payout_tool
(
    id                                                             bigserial                   NOT NULL,
    cntrct_id                                                      bigint                      NOT NULL,
    payout_tool_id                                                 character varying           NOT NULL,
    created_at                                                     timestamp without time zone NOT NULL,
    currency_code                                                  character varying           NOT NULL,
    payout_tool_info                                               dw.payout_tool_info         NOT NULL,
    payout_tool_info_russian_bank_account                          character varying,
    payout_tool_info_russian_bank_name                             character varying,
    payout_tool_info_russian_bank_post_account                     character varying,
    payout_tool_info_russian_bank_bik                              character varying,
    payout_tool_info_international_bank_account_holder             character varying,
    payout_tool_info_international_bank_name                       character varying,
    payout_tool_info_international_bank_address                    character varying,
    payout_tool_info_international_bank_iban                       character varying,
    payout_tool_info_international_bank_bic                        character varying,
    payout_tool_info_international_bank_local_code                 character varying,
    payout_tool_info_international_bank_number                     character varying,
    payout_tool_info_international_bank_aba_rtn                    character varying,
    payout_tool_info_international_bank_country_code               character varying,
    payout_tool_info_international_correspondent_bank_account      character varying,
    payout_tool_info_international_correspondent_bank_name         character varying,
    payout_tool_info_international_correspondent_bank_address      character varying,
    payout_tool_info_international_correspondent_bank_bic          character varying,
    payout_tool_info_international_correspondent_bank_iban         character varying,
    payout_tool_info_international_correspondent_bank_number       character varying,
    payout_tool_info_international_correspondent_bank_aba_rtn      character varying,
    payout_tool_info_international_correspondent_bank_country_code character varying,
    payout_tool_info_wallet_info_wallet_id                         character varying,
    CONSTRAINT payout_tool_pkey PRIMARY KEY (id)
);

CREATE INDEX payout_tool_idx ON dw.payout_tool USING btree (cntrct_id);


CREATE TABLE dw.provider
(
    id                           bigserial                                                        NOT NULL,
    version_id                   bigint                                                           NOT NULL,
    provider_ref_id              integer                                                          NOT NULL,
    name                         character varying                                                NOT NULL,
    description                  character varying                                                NOT NULL,
    proxy_ref_id                 integer                                                          NOT NULL,
    proxy_additional_json        character varying                                                NOT NULL,
    terminal_json                character varying,
    abs_account                  character varying,
    payment_terms_json           character varying,
    recurrent_paytool_terms_json character varying,
    accounts_json                character varying,
    wtime                        timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                      boolean                     DEFAULT true                         NOT NULL,
    identity                     character varying,
    wallet_terms_json            character varying,
    params_schema_json           character varying,
    CONSTRAINT provider_pkey PRIMARY KEY (id)
);

CREATE INDEX provider_idx ON dw.provider USING btree (provider_ref_id);
CREATE INDEX provider_version_id ON dw.provider USING btree (version_id);

CREATE TABLE dw.proxy
(
    id           bigserial                                                        NOT NULL,
    version_id   bigint                                                           NOT NULL,
    proxy_ref_id integer                                                          NOT NULL,
    name         character varying                                                NOT NULL,
    description  character varying                                                NOT NULL,
    url          character varying                                                NOT NULL,
    options_json character varying                                                NOT NULL,
    wtime        timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current      boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT proxy_pkey PRIMARY KEY (id)
);

CREATE INDEX proxy_idx ON dw.proxy USING btree (proxy_ref_id);
CREATE INDEX proxy_version_id ON dw.proxy USING btree (version_id);


CREATE TABLE dw.rate
(
    id                        bigserial                                                        NOT NULL,
    event_created_at          timestamp without time zone                                      NOT NULL,
    source_id                 character varying                                                NOT NULL,
    lower_bound_inclusive     timestamp without time zone                                      NOT NULL,
    upper_bound_exclusive     timestamp without time zone                                      NOT NULL,
    source_symbolic_code      character varying                                                NOT NULL,
    source_exponent           smallint                                                         NOT NULL,
    destination_symbolic_code character varying                                                NOT NULL,
    destination_exponent      smallint                                                         NOT NULL,
    exchange_rate_rational_p  bigint                                                           NOT NULL,
    exchange_rate_rational_q  bigint                                                           NOT NULL,
    wtime                     timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                   boolean                     DEFAULT true                         NOT NULL,
    sequence_id               bigint,
    CONSTRAINT rate_pkey PRIMARY KEY (id)
);

CREATE INDEX rate_event_created_at_idx ON dw.rate USING btree (event_created_at);
CREATE INDEX rate_source_id_idx ON dw.rate USING btree (source_id);
CREATE UNIQUE INDEX rate_ukey ON dw.rate USING btree (source_id, sequence_id, source_symbolic_code,
                                                      destination_symbolic_code);


CREATE TABLE dw.recurrent_payment_tool
(
    id                                                        bigserial                                                        NOT NULL,
    sequence_id                                               integer                                                          NOT NULL,
    change_id                                                 integer                                                          NOT NULL,
    event_created_at                                          timestamp without time zone                                      NOT NULL,
    recurrent_payment_tool_id                                 character varying                                                NOT NULL,
    created_at                                                timestamp without time zone                                      NOT NULL,
    party_id                                                  character varying                                                NOT NULL,
    shop_id                                                   character varying                                                NOT NULL,
    party_revision                                            bigint,
    domain_revision                                           bigint                                                           NOT NULL,
    status                                                    dw.recurrent_payment_tool_status                                 NOT NULL,
    status_failed_failure                                     character varying,
    payment_tool_type                                         dw.payment_tool_type                                             NOT NULL,
    bank_card_token                                           character varying,
    bank_card_payment_system                                  character varying,
    bank_card_bin                                             character varying,
    bank_card_masked_pan                                      character varying,
    bank_card_token_provider                                  character varying,
    bank_card_issuer_country                                  character varying,
    bank_card_bank_name                                       character varying,
    bank_card_metadata_json                                   character varying,
    bank_card_is_cvv_empty                                    boolean,
    bank_card_exp_date_month                                  integer,
    bank_card_exp_date_year                                   integer,
    bank_card_cardholder_name                                 character varying,
    payment_terminal_type                                     character varying,
    digital_wallet_provider                                   character varying,
    digital_wallet_id                                         character varying,
    digital_wallet_token                                      character varying,
    crypto_currency                                           character varying,
    mobile_commerce_operator_legacy                           dw.mobile_operator_type,
    mobile_commerce_phone_cc                                  character varying,
    mobile_commerce_phone_ctn                                 character varying,
    payment_session_id                                        character varying,
    client_info_ip_address                                    character varying,
    client_info_fingerprint                                   character varying,
    rec_token                                                 character varying,
    route_provider_id                                         integer,
    route_terminal_id                                         integer,
    amount                                                    bigint,
    currency_code                                             character varying,
    risk_score                                                character varying,
    session_payload_transaction_bound_trx_id                  character varying,
    session_payload_transaction_bound_trx_extra_json          character varying,
    session_payload_transaction_bound_trx_additional_info_rrn character varying,
    wtime                                                     timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                                   boolean                     DEFAULT true                         NOT NULL,
    mobile_commerce_operator                                  character varying,
    CONSTRAINT recurrent_payment_tool_pkey PRIMARY KEY (id),
    CONSTRAINT recurrent_payment_tool_uniq UNIQUE (recurrent_payment_tool_id, sequence_id, change_id)
);

CREATE INDEX recurrent_payment_tool_id_idx ON dw.recurrent_payment_tool USING btree (recurrent_payment_tool_id);

CREATE TABLE dw.refund
(
    id                                               bigserial                                                        NOT NULL,
    event_created_at                                 timestamp without time zone                                      NOT NULL,
    domain_revision                                  bigint                                                           NOT NULL,
    refund_id                                        character varying                                                NOT NULL,
    payment_id                                       character varying                                                NOT NULL,
    invoice_id                                       character varying                                                NOT NULL,
    party_id                                         character varying                                                NOT NULL,
    shop_id                                          character varying                                                NOT NULL,
    created_at                                       timestamp without time zone                                      NOT NULL,
    status                                           dw.refund_status                                                 NOT NULL,
    status_failed_failure                            character varying,
    amount                                           bigint,
    currency_code                                    character varying,
    reason                                           character varying,
    wtime                                            timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                          boolean                     DEFAULT true                         NOT NULL,
    session_payload_transaction_bound_trx_id         character varying,
    session_payload_transaction_bound_trx_extra_json character varying,
    fee                                              bigint,
    provider_fee                                     bigint,
    external_fee                                     bigint,
    party_revision                                   bigint,
    sequence_id                                      bigint,
    change_id                                        integer,
    external_id                                      character varying,
    CONSTRAINT refund_pkey PRIMARY KEY (id),
    CONSTRAINT refund_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX refund_created_at ON dw.refund USING btree (created_at);
CREATE INDEX refund_event_created_at ON dw.refund USING btree (event_created_at);
CREATE INDEX refund_external_id_idx ON dw.refund USING btree (external_id) WHERE (external_id IS NOT NULL);
CREATE INDEX refund_invoice_id ON dw.refund USING btree (invoice_id);
CREATE INDEX refund_party_id ON dw.refund USING btree (party_id);
CREATE INDEX refund_status ON dw.refund USING btree (status);


CREATE TABLE dw.shedlock
(
    name       character varying(64) NOT NULL,
    lock_until timestamp(3) without time zone,
    locked_at  timestamp(3) without time zone,
    locked_by  character varying(255),
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);

CREATE TABLE dw.shop
(
    id                         bigserial                                                        NOT NULL,
    event_created_at           timestamp without time zone                                      NOT NULL,
    party_id                   character varying                                                NOT NULL,
    shop_id                    character varying                                                NOT NULL,
    created_at                 timestamp without time zone                                      NOT NULL,
    blocking                   dw.blocking                                                      NOT NULL,
    blocking_unblocked_reason  character varying,
    blocking_unblocked_since   timestamp without time zone,
    blocking_blocked_reason    character varying,
    blocking_blocked_since     timestamp without time zone,
    suspension                 dw.suspension                                                    NOT NULL,
    suspension_active_since    timestamp without time zone,
    suspension_suspended_since timestamp without time zone,
    details_name               character varying                                                NOT NULL,
    details_description        character varying,
    location_url               character varying                                                NOT NULL,
    category_id                integer                                                          NOT NULL,
    account_currency_code      character varying,
    account_settlement         bigint,
    account_guarantee          bigint,
    account_payout             bigint,
    contract_id                character varying                                                NOT NULL,
    payout_tool_id             character varying,
    payout_schedule_id         integer,
    wtime                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                    boolean                     DEFAULT true                         NOT NULL,
    sequence_id                integer,
    change_id                  integer,
    claim_effect_id            integer,
    CONSTRAINT shop_pkey PRIMARY KEY (id),
    CONSTRAINT shop_uniq UNIQUE (party_id, shop_id, sequence_id, change_id, claim_effect_id)
);

CREATE INDEX shop_created_at ON dw.shop USING btree (created_at);
CREATE INDEX shop_event_created_at ON dw.shop USING btree (event_created_at);
CREATE INDEX shop_party_id ON dw.shop USING btree (party_id);
CREATE INDEX shop_shop_id ON dw.shop USING btree (shop_id);


CREATE TABLE dw.shop_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT shop_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX shop_revision_idx ON dw.shop_revision USING btree (obj_id, revision);


CREATE TABLE dw.source
(
    id                        bigserial                                                        NOT NULL,
    event_created_at          timestamp without time zone                                      NOT NULL,
    event_occured_at          timestamp without time zone                                      NOT NULL,
    sequence_id               integer                                                          NOT NULL,
    source_id                 character varying                                                NOT NULL,
    source_name               character varying                                                NOT NULL,
    source_status             dw.source_status                                                 NOT NULL,
    resource_internal_details character varying,
    account_id                character varying,
    identity_id               character varying,
    party_id                  character varying,
    accounter_account_id      bigint,
    currency_code             character varying,
    wtime                     timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                   boolean                     DEFAULT true                         NOT NULL,
    external_id               character varying,
    CONSTRAINT source_pkey PRIMARY KEY (id),
    CONSTRAINT source_uniq UNIQUE (source_id, sequence_id)
);

CREATE INDEX source_event_created_at_idx ON dw.source USING btree (event_created_at);
CREATE INDEX source_event_occured_at_idx ON dw.source USING btree (event_occured_at);
CREATE INDEX source_id_idx ON dw.source USING btree (source_id);


CREATE TABLE dw.term_set_hierarchy
(
    id                        bigserial                                                        NOT NULL,
    version_id                bigint                                                           NOT NULL,
    term_set_hierarchy_ref_id integer                                                          NOT NULL,
    name                      character varying,
    description               character varying,
    parent_terms_ref_id       integer,
    term_sets_json            character varying                                                NOT NULL,
    wtime                     timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                   boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT term_set_hierarchy_pkey PRIMARY KEY (id)
);

CREATE INDEX term_set_hierarchy_idx ON dw.term_set_hierarchy USING btree (term_set_hierarchy_ref_id);
CREATE INDEX term_set_hierarchy_version_id ON dw.term_set_hierarchy USING btree (version_id);


CREATE TABLE dw.terminal
(
    id                       bigserial                                                        NOT NULL,
    version_id               bigint                                                           NOT NULL,
    terminal_ref_id          integer                                                          NOT NULL,
    name                     character varying                                                NOT NULL,
    description              character varying                                                NOT NULL,
    options_json             character varying,
    risk_coverage            character varying,
    terms_json               character varying,
    wtime                    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                  boolean                     DEFAULT true                         NOT NULL,
    external_terminal_id     character varying,
    external_merchant_id     character varying,
    mcc                      character varying,
    terminal_provider_ref_id integer,
    CONSTRAINT terminal_pkey PRIMARY KEY (id)
);

CREATE INDEX terminal_idx ON dw.terminal USING btree (terminal_ref_id);
CREATE INDEX terminal_version_id ON dw.terminal USING btree (version_id);


CREATE TABLE dw.trade_bloc
(
    id                bigserial                                                        NOT NULL,
    version_id        bigint                                                           NOT NULL,
    trade_bloc_ref_id character varying                                                NOT NULL,
    name              character varying                                                NOT NULL,
    description       character varying,
    wtime             timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current           boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT trade_bloc_pkey PRIMARY KEY (id)
);

CREATE TABLE dw.wallet
(
    id                   bigserial                                                        NOT NULL,
    event_created_at     timestamp without time zone                                      NOT NULL,
    event_occured_at     timestamp without time zone                                      NOT NULL,
    sequence_id          integer                                                          NOT NULL,
    wallet_id            character varying                                                NOT NULL,
    wallet_name          character varying                                                NOT NULL,
    identity_id          character varying,
    party_id             character varying,
    currency_code        character varying,
    wtime                timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current              boolean                     DEFAULT true                         NOT NULL,
    account_id           character varying,
    accounter_account_id bigint,
    external_id          character varying,
    CONSTRAINT wallet_pkey PRIMARY KEY (id),
    CONSTRAINT wallet_uniq UNIQUE (wallet_id, sequence_id)
);

CREATE INDEX wallet_event_created_at_idx ON dw.wallet USING btree (event_created_at);
CREATE INDEX wallet_event_occured_at_idx ON dw.wallet USING btree (event_occured_at);
CREATE INDEX wallet_id_idx ON dw.wallet USING btree (wallet_id);


CREATE TABLE dw.withdrawal
(
    id                                    bigserial                                                        NOT NULL,
    event_created_at                      timestamp without time zone                                      NOT NULL,
    event_occured_at                      timestamp without time zone                                      NOT NULL,
    sequence_id                           integer                                                          NOT NULL,
    wallet_id                             character varying                                                NOT NULL,
    destination_id                        character varying                                                NOT NULL,
    withdrawal_id                         character varying                                                NOT NULL,
    provider_id_legacy                    character varying,
    amount                                bigint                                                           NOT NULL,
    currency_code                         character varying                                                NOT NULL,
    withdrawal_status                     dw.withdrawal_status                                             NOT NULL,
    withdrawal_transfer_status            dw.withdrawal_transfer_status,
    wtime                                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                               boolean                     DEFAULT true                         NOT NULL,
    fee                                   bigint,
    provider_fee                          bigint,
    external_id                           character varying,
    context_json                          character varying,
    withdrawal_status_failed_failure_json character varying,
    provider_id                           integer,
    CONSTRAINT withdrawal_pkey PRIMARY KEY (id),
    CONSTRAINT withdrawal_uniq UNIQUE (withdrawal_id, sequence_id)
);

CREATE INDEX withdrawal_event_created_at_idx ON dw.withdrawal USING btree (event_created_at);
CREATE INDEX withdrawal_event_occured_at_idx ON dw.withdrawal USING btree (event_occured_at);
CREATE INDEX withdrawal_id_idx ON dw.withdrawal USING btree (withdrawal_id);


CREATE TABLE dw.withdrawal_provider
(
    id                         bigserial                                                        NOT NULL,
    version_id                 bigint                                                           NOT NULL,
    withdrawal_provider_ref_id integer                                                          NOT NULL,
    name                       character varying                                                NOT NULL,
    description                character varying,
    proxy_ref_id               integer                                                          NOT NULL,
    proxy_additional_json      character varying                                                NOT NULL,
    identity                   character varying,
    withdrawal_terms_json      character varying,
    accounts_json              character varying,
    wtime                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                    boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT withdrawal_provider_pkey PRIMARY KEY (id)
);

CREATE INDEX withdrawal_provider_idx ON dw.withdrawal_provider USING btree (withdrawal_provider_ref_id);
CREATE INDEX withdrawal_provider_version_id ON dw.withdrawal_provider USING btree (version_id);


CREATE TABLE dw.withdrawal_session
(
    id                                bigserial                                                        NOT NULL,
    event_created_at                  timestamp without time zone                                      NOT NULL,
    event_occured_at                  timestamp without time zone                                      NOT NULL,
    sequence_id                       integer                                                          NOT NULL,
    withdrawal_session_id             character varying                                                NOT NULL,
    withdrawal_session_status         dw.withdrawal_session_status                                     NOT NULL,
    provider_id_legacy                character varying,
    withdrawal_id                     character varying                                                NOT NULL,
    destination_card_token            character varying,
    destination_card_payment_system   dw.bank_card_payment_system,
    destination_card_bin              character varying,
    destination_card_masked_pan       character varying,
    amount                            bigint                                                           NOT NULL,
    currency_code                     character varying                                                NOT NULL,
    sender_party_id                   character varying,
    sender_provider_id                character varying,
    sender_class_id                   character varying,
    sender_contract_id                character varying,
    receiver_party_id                 character varying,
    receiver_provider_id              character varying,
    receiver_class_id                 character varying,
    receiver_contract_id              character varying,
    adapter_state                     character varying,
    tran_info_id                      character varying,
    tran_info_timestamp               timestamp without time zone,
    tran_info_json                    character varying,
    wtime                             timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                           boolean                     DEFAULT true                         NOT NULL,
    failure_json                      character varying,
    resource_type                     dw.destination_resource_type                                     NOT NULL,
    resource_crypto_wallet_id         character varying,
    resource_crypto_wallet_type       character varying,
    resource_crypto_wallet_data       character varying,
    resource_bank_card_type           character varying,
    resource_bank_card_issuer_country character varying,
    resource_bank_card_bank_name      character varying,
    tran_additional_info              character varying,
    tran_additional_info_rrn          character varying,
    tran_additional_info_json         character varying,
    provider_id                       integer,
    resource_digital_wallet_id        character varying,
    resource_digital_wallet_data      character varying,
    CONSTRAINT withdrawal_session_pk PRIMARY KEY (id),
    CONSTRAINT withdrawal_session_uniq UNIQUE (withdrawal_session_id, sequence_id)
);

CREATE INDEX withdrawal_session_event_created_at_idx ON dw.withdrawal_session USING btree (event_created_at);
CREATE INDEX withdrawal_session_event_occured_at_idx ON dw.withdrawal_session USING btree (event_occured_at);
CREATE INDEX withdrawal_session_id_idx ON dw.withdrawal_session USING btree (withdrawal_session_id);