CREATE SCHEMA IF NOT EXISTS nw;

CREATE TYPE nw.adjustment_cash_flow_type AS ENUM (
    'new_cash_flow',
    'old_cash_flow_inverse'
    );

CREATE TYPE nw.adjustment_status AS ENUM (
    'pending',
    'captured',
    'cancelled',
    'processed'
    );

CREATE TYPE nw.bank_card_payment_system AS ENUM (
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

CREATE TYPE nw.blocking AS ENUM (
    'unblocked',
    'blocked'
    );

CREATE TYPE nw.cash_flow_account AS ENUM (
    'merchant',
    'provider',
    'system',
    'external',
    'wallet'
    );

CREATE TYPE nw.challenge_resolution AS ENUM (
    'approved',
    'denied'
    );

CREATE TYPE nw.challenge_status AS ENUM (
    'pending',
    'cancelled',
    'completed',
    'failed'
    );

CREATE TYPE nw.chargeback_category AS ENUM (
    'fraud',
    'dispute',
    'authorisation',
    'processing_error'
    );

CREATE TYPE nw.chargeback_stage AS ENUM (
    'chargeback',
    'pre_arbitration',
    'arbitration'
    );

CREATE TYPE nw.chargeback_status AS ENUM (
    'pending',
    'accepted',
    'rejected',
    'cancelled'
    );

CREATE TYPE nw.contract_status AS ENUM (
    'active',
    'terminated',
    'expired'
    );

CREATE TYPE nw.contractor_type AS ENUM (
    'registered_user',
    'legal_entity',
    'private_entity'
    );

CREATE TYPE nw.deposit_adjustment_status AS ENUM (
    'pending',
    'succeeded'
    );

CREATE TYPE nw.deposit_revert_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE nw.deposit_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE nw.deposit_transfer_status AS ENUM (
    'created',
    'prepared',
    'committed',
    'cancelled'
    );

CREATE TYPE nw.destination_resource_type AS ENUM (
    'bank_card',
    'crypto_wallet',
    'digital_wallet',
    'generic'
    );

CREATE TYPE nw.destination_status AS ENUM (
    'authorized',
    'unauthorized'
    );

CREATE TYPE nw.fistful_cash_flow_change_type AS ENUM (
    'withdrawal',
    'deposit',
    'deposit_revert',
    'deposit_adjustment'
    );

CREATE TYPE nw.invoice_status AS ENUM (
    'unpaid',
    'paid',
    'cancelled',
    'fulfilled'
    );

CREATE TYPE nw.legal_entity AS ENUM (
    'russian_legal_entity',
    'international_legal_entity'
    );

CREATE TYPE nw.mobile_operator_type AS ENUM (
    'mts',
    'beeline',
    'megafone',
    'tele2',
    'yota'
    );

CREATE TYPE nw.payer_type AS ENUM (
    'payment_resource',
    'customer',
    'recurrent'
    );

CREATE TYPE nw.payment_change_type AS ENUM (
    'payment',
    'refund',
    'adjustment',
    'payout',
    'chargeback'
    );

CREATE TYPE nw.payment_flow_type AS ENUM (
    'instant',
    'hold'
    );

CREATE TYPE nw.payment_method_type AS ENUM (
    'bank_card',
    'payment_terminal',
    'digital_wallet',
    'tokenized_bank_card',
    'empty_cvv_bank_card',
    'crypto_currency',
    'mobile',
    'generic'
    );

CREATE TYPE nw.payment_status AS ENUM (
    'pending',
    'processed',
    'captured',
    'cancelled',
    'refunded',
    'failed',
    'charged_back'
    );

CREATE TYPE nw.payment_tool_type AS ENUM (
    'bank_card',
    'payment_terminal',
    'digital_wallet',
    'crypto_currency',
    'mobile_commerce',
    'crypto_currency_deprecated'
    );

CREATE TYPE nw.payout_account_type AS ENUM (
    'russian_payout_account',
    'international_payout_account'
    );

CREATE TYPE nw.payout_paid_status_details AS ENUM (
    'card_details',
    'account_details'
    );

CREATE TYPE nw.payout_status AS ENUM (
    'unpaid',
    'paid',
    'cancelled',
    'confirmed'
    );

CREATE TYPE nw.payout_tool_info AS ENUM (
    'russian_bank_account',
    'international_bank_account',
    'wallet_info',
    'payment_institution_account'
    );

CREATE TYPE nw.payout_type AS ENUM (
    'bank_card',
    'bank_account',
    'wallet'
    );

CREATE TYPE nw.private_entity AS ENUM (
    'russian_private_entity'
    );

CREATE TYPE nw.recurrent_payment_tool_status AS ENUM (
    'created',
    'acquired',
    'abandoned',
    'failed'
    );

CREATE TYPE nw.refund_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE nw.representative_document AS ENUM (
    'articles_of_association',
    'power_of_attorney',
    'expired'
    );

CREATE TYPE nw.risk_score AS ENUM (
    'low',
    'high',
    'fatal'
    );

CREATE TYPE nw.source_status AS ENUM (
    'authorized',
    'unauthorized'
    );

CREATE TYPE nw.suspension AS ENUM (
    'active',
    'suspended'
    );

CREATE TYPE nw.user_type AS ENUM (
    'internal_user',
    'external_user',
    'service_user'
    );

CREATE TYPE nw.withdrawal_session_status AS ENUM (
    'active',
    'success',
    'failed'
    );

CREATE TYPE nw.withdrawal_status AS ENUM (
    'pending',
    'succeeded',
    'failed'
    );

CREATE TYPE nw.withdrawal_transfer_status AS ENUM (
    'created',
    'prepared',
    'committed',
    'cancelled'
    );


-- TABLES
CREATE TABLE nw.adjustment
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
    status                       nw.adjustment_status                                             NOT NULL,
    status_captured_at           timestamp without time zone,
    status_cancelled_at          timestamp without time zone,
    reason                       character varying                                                NOT NULL,
    wtime                        timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                      boolean                     DEFAULT true                         NOT NULL,
    party_revision               bigint,
    sequence_id                  bigint,
    change_id                    integer,
    payment_status               nw.payment_status,
    amount                       bigint                                                           NOT NULL,
    provider_amount_diff         bigint                      DEFAULT 0,
    system_amount_diff           bigint                      DEFAULT 0,
    external_income_amount_diff  bigint                      DEFAULT 0,
    external_outcome_amount_diff bigint                      DEFAULT 0,
    CONSTRAINT adjustment_pkey PRIMARY KEY (id),
    CONSTRAINT adjustment_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX adjustment_created_at ON nw.adjustment USING btree (created_at);
CREATE INDEX adjustment_event_created_at ON nw.adjustment USING btree (event_created_at);
CREATE INDEX adjustment_invoice_id ON nw.adjustment USING btree (invoice_id);
CREATE INDEX adjustment_party_id ON nw.adjustment USING btree (party_id);
CREATE INDEX adjustment_status ON nw.adjustment USING btree (status);


CREATE TABLE nw.cash_flow
(
    id                             bigserial              NOT NULL,
    obj_id                         bigint                 NOT NULL,
    obj_type                       nw.payment_change_type NOT NULL,
    adj_flow_type                  nw.adjustment_cash_flow_type,
    source_account_type            nw.cash_flow_account   NOT NULL,
    source_account_type_value      character varying      NOT NULL,
    source_account_id              bigint                 NOT NULL,
    destination_account_type       nw.cash_flow_account   NOT NULL,
    destination_account_type_value character varying      NOT NULL,
    destination_account_id         bigint                 NOT NULL,
    amount                         bigint                 NOT NULL,
    currency_code                  character varying      NOT NULL,
    details                        character varying,
    CONSTRAINT cash_flow_pkey PRIMARY KEY (id)
);

CREATE INDEX cash_flow_idx ON nw.cash_flow USING btree (obj_id, obj_type);


CREATE TABLE nw.calendar
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

CREATE INDEX calendar_idx ON nw.calendar USING btree (calendar_ref_id);
CREATE INDEX calendar_version_id ON nw.calendar USING btree (version_id);


CREATE TABLE nw.category
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

CREATE INDEX category_idx ON nw.category USING btree (category_ref_id);
CREATE INDEX category_version_id ON nw.category USING btree (version_id);


CREATE TABLE nw.challenge
(
    id                    bigserial                                                        NOT NULL,
    event_created_at      timestamp without time zone                                      NOT NULL,
    event_occured_at      timestamp without time zone                                      NOT NULL,
    sequence_id           integer                                                          NOT NULL,
    identity_id           character varying                                                NOT NULL,
    challenge_id          character varying                                                NOT NULL,
    challenge_class_id    character varying                                                NOT NULL,
    challenge_status      nw.challenge_status                                              NOT NULL,
    challenge_resolution  nw.challenge_resolution,
    challenge_valid_until timestamp without time zone,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current               boolean                     DEFAULT true                         NOT NULL,
    proofs_json           character varying,
    CONSTRAINT challenge_pkey PRIMARY KEY (id),
    CONSTRAINT challenge_uniq UNIQUE (challenge_id, identity_id, sequence_id)
);

CREATE INDEX challenge_event_created_at_idx ON nw.challenge USING btree (event_created_at);
CREATE INDEX challenge_event_occured_at_idx ON nw.challenge USING btree (event_occured_at);
CREATE INDEX challenge_id_idx ON nw.challenge USING btree (challenge_id);


CREATE TABLE nw.chargeback
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
    status             nw.chargeback_status                                             NOT NULL,
    levy_amount        bigint,
    levy_currency_code character varying,
    amount             bigint,
    currency_code      character varying,
    reason_code        character varying,
    reason_category    nw.chargeback_category                                           NOT NULL,
    stage              nw.chargeback_stage                                              NOT NULL,
    current            boolean                     DEFAULT true                         NOT NULL,
    context            bytea,
    wtime              timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT chargeback_pkey PRIMARY KEY (id),
    CONSTRAINT chargeback_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX chargeback_created_at ON nw.chargeback USING btree (created_at);
CREATE INDEX chargeback_event_created_at ON nw.chargeback USING btree (event_created_at);
CREATE INDEX chargeback_invoice_id ON nw.chargeback USING btree (invoice_id);
CREATE INDEX chargeback_party_id ON nw.chargeback USING btree (party_id);
CREATE INDEX chargeback_status ON nw.chargeback USING btree (status);


CREATE TABLE nw.contract
(
    id                                                         bigserial                                                        NOT NULL,
    event_created_at                                           timestamp without time zone                                      NOT NULL,
    contract_id                                                character varying                                                NOT NULL,
    party_id                                                   character varying                                                NOT NULL,
    payment_institution_id                                     integer,
    created_at                                                 timestamp without time zone                                      NOT NULL,
    valid_since                                                timestamp without time zone,
    valid_until                                                timestamp without time zone,
    status                                                     nw.contract_status                                               NOT NULL,
    status_terminated_at                                       timestamp without time zone,
    terms_id                                                   integer                                                          NOT NULL,
    legal_agreement_signed_at                                  timestamp without time zone,
    legal_agreement_id                                         character varying,
    legal_agreement_valid_until                                timestamp without time zone,
    report_act_schedule_id                                     integer,
    report_act_signer_position                                 character varying,
    report_act_signer_full_name                                character varying,
    report_act_signer_document                                 nw.representative_document,
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

CREATE INDEX contract_contract_id ON nw.contract USING btree (contract_id);
CREATE INDEX contract_created_at ON nw.contract USING btree (created_at);
CREATE INDEX contract_event_created_at ON nw.contract USING btree (event_created_at);
CREATE INDEX contract_party_id ON nw.contract USING btree (party_id);


CREATE TABLE nw.contract_adjustment
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

CREATE INDEX contract_adjustment_idx ON nw.contract_adjustment USING btree (cntrct_id);


CREATE TABLE nw.contract_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT contract_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX contract_revision_idx ON nw.contract_revision USING btree (obj_id, revision);


CREATE TABLE nw.contractor
(
    id                                             bigserial                                                        NOT NULL,
    event_created_at                               timestamp without time zone                                      NOT NULL,
    party_id                                       character varying                                                NOT NULL,
    contractor_id                                  character varying                                                NOT NULL,
    type                                           nw.contractor_type                                               NOT NULL,
    identificational_level                         character varying,
    registered_user_email                          character varying,
    legal_entity                                   nw.legal_entity,
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
    private_entity                                 nw.private_entity,
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

CREATE INDEX contractor_contractor_id ON nw.contractor USING btree (contractor_id);
CREATE INDEX contractor_event_created_at ON nw.contractor USING btree (event_created_at);
CREATE INDEX contractor_party_id ON nw.contractor USING btree (party_id);


CREATE TABLE nw.contractor_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT contractor_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX contractor_revision_idx ON nw.contractor_revision USING btree (obj_id, revision);


CREATE TABLE nw.country
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



CREATE TABLE nw.currency
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

CREATE INDEX currency_idx ON nw.currency USING btree (currency_ref_id);
CREATE INDEX currency_version_id ON nw.currency USING btree (version_id);


CREATE TABLE nw.deposit
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
    deposit_status          nw.deposit_status                                                NOT NULL,
    deposit_transfer_status nw.deposit_transfer_status,
    wtime                   timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                 boolean                     DEFAULT true                         NOT NULL,
    external_id             character varying,
    CONSTRAINT deposit_pkey PRIMARY KEY (id),
    CONSTRAINT deposit_uniq UNIQUE (deposit_id, sequence_id)
);

CREATE INDEX deposit_event_created_at_idx ON nw.deposit USING btree (event_created_at);
CREATE INDEX deposit_event_occured_at_idx ON nw.deposit USING btree (event_occured_at);
CREATE INDEX deposit_id_idx ON nw.deposit USING btree (deposit_id);


CREATE TABLE nw.deposit_adjustment
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
    status           nw.deposit_adjustment_status                                     NOT NULL,
    transfer_status  nw.deposit_transfer_status,
    deposit_status   nw.deposit_status,
    external_id      character varying,
    wtime            timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current          boolean                     DEFAULT true                         NOT NULL,
    party_revision   bigint                      DEFAULT 0                            NOT NULL,
    domain_revision  bigint                      DEFAULT 0                            NOT NULL,
    CONSTRAINT deposit_adjustment_pkey PRIMARY KEY (id),
    CONSTRAINT deposit_adjustment_uniq UNIQUE (deposit_id, adjustment_id, sequence_id)
);

CREATE INDEX deposit_adjustment_event_created_at_idx ON nw.deposit_adjustment USING btree (event_created_at);
CREATE INDEX deposit_adjustment_id_idx ON nw.deposit_adjustment USING btree (deposit_id);


CREATE TABLE nw.deposit_revert
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
    status           nw.deposit_revert_status                                         NOT NULL,
    transfer_status  nw.deposit_transfer_status,
    reason           character varying,
    external_id      character varying,
    wtime            timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current          boolean                     DEFAULT true                         NOT NULL,
    party_revision   bigint                      DEFAULT 0                            NOT NULL,
    domain_revision  bigint                      DEFAULT 0                            NOT NULL,
    CONSTRAINT deposit_revert_pkey PRIMARY KEY (id),
    CONSTRAINT deposit_revert_uniq UNIQUE (deposit_id, revert_id, sequence_id)
);

CREATE INDEX deposit_revert_event_created_at_idx ON nw.deposit_revert USING btree (event_created_at);
CREATE INDEX deposit_revert_id_idx ON nw.deposit_revert USING btree (deposit_id);


CREATE TABLE nw.destination
(
    id                                bigserial                                                        NOT NULL,
    event_created_at                  timestamp without time zone                                      NOT NULL,
    event_occured_at                  timestamp without time zone                                      NOT NULL,
    sequence_id                       integer                                                          NOT NULL,
    destination_id                    character varying                                                NOT NULL,
    destination_name                  character varying                                                NOT NULL,
    destination_status                nw.destination_status                                            NOT NULL,
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
    resource_type                     nw.destination_resource_type                                     NOT NULL,
    resource_crypto_wallet_data       character varying,
    resource_bank_card_type           character varying,
    resource_bank_card_issuer_country character varying,
    resource_bank_card_bank_name      character varying,
    resource_digital_wallet_id        character varying,
    resource_digital_wallet_data      character varying,
    CONSTRAINT destination_pkey PRIMARY KEY (id),
    CONSTRAINT destination_uniq UNIQUE (destination_id, sequence_id)
);

CREATE INDEX destination_event_created_at_idx ON nw.destination USING btree (event_created_at);
CREATE INDEX destination_event_occured_at_idx ON nw.destination USING btree (event_occured_at);
CREATE INDEX destination_id_idx ON nw.destination USING btree (destination_id);


CREATE TABLE nw.fistful_cash_flow
(
    id                             bigserial                        NOT NULL,
    obj_id                         bigint                           NOT NULL,
    source_account_type            nw.cash_flow_account             NOT NULL,
    source_account_type_value      character varying                NOT NULL,
    source_account_id              character varying                NOT NULL,
    destination_account_type       nw.cash_flow_account             NOT NULL,
    destination_account_type_value character varying                NOT NULL,
    destination_account_id         character varying                NOT NULL,
    amount                         bigint                           NOT NULL,
    currency_code                  character varying                NOT NULL,
    details                        character varying,
    obj_type                       nw.fistful_cash_flow_change_type NOT NULL,
    CONSTRAINT fistful_cash_flow_pkey PRIMARY KEY (id)
);

CREATE INDEX fistful_cash_flow_obj_id_idx ON nw.fistful_cash_flow USING btree (obj_id);


CREATE TABLE nw.identity
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

CREATE INDEX identity_event_created_at_idx ON nw.identity USING btree (event_created_at);
CREATE INDEX identity_event_occured_at_idx ON nw.identity USING btree (event_occured_at);
CREATE INDEX identity_id_idx ON nw.identity USING btree (identity_id);
CREATE INDEX identity_party_id_idx ON nw.identity USING btree (party_id);


CREATE TABLE nw.inspector
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

CREATE INDEX inspector_idx ON nw.inspector USING btree (inspector_ref_id);
CREATE INDEX inspector_version_id ON nw.inspector USING btree (version_id);


CREATE TABLE IF NOT EXISTS nw.invoice
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

-- TODO: why do we need start value? confirm and rework
ALTER SEQUENCE nw.invoice_id_seq
    MINVALUE 200000000
    START with 200000000
    RESTART
    NO MAXVALUE
    CACHE 1;

-- TODO check for necessary indexes
CREATE INDEX invoice_created_at ON nw.invoice USING btree (created_at);
CREATE INDEX invoice_event_created_at ON nw.invoice USING btree (event_created_at);
CREATE INDEX invoice_external_id_idx ON nw.invoice USING btree (external_id) WHERE (external_id IS NOT NULL);
CREATE INDEX invoice_invoice_id ON nw.invoice USING btree (invoice_id);
CREATE INDEX invoice_party_id ON nw.invoice USING btree (party_id);

CREATE TABLE IF NOT EXISTS nw.invoice_status_info
(
    id               BIGSERIAL,
    event_created_at timestamp without time zone                    NOT NULL,
    invoice_id       character varying COLLATE pg_catalog."default" NOT NULL,
    status           nw.invoice_status                              NOT NULL,
    details          character varying COLLATE pg_catalog."default",
    wtime            timestamp without time zone                    NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
    current          boolean                                        DEFAULT false NOT NULL,
    sequence_id      bigint,
    change_id        integer,
    external_id      character varying COLLATE pg_catalog."default",
    CONSTRAINT invoice_status_pkey PRIMARY KEY (id),
    CONSTRAINT invoice_status_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

-- TODO should it be here?
CREATE INDEX invoice_status ON nw.invoice_status_info USING btree (status);
-- TODO: confirm this one. maybe use sequence_id or just add it to timestamp?
CREATE INDEX current_invoice_status ON nw.invoice_status_info USING btree (invoice_id, wtime DESC);

CREATE TABLE IF NOT EXISTS nw.invoice_cart
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

-- TODO should it be here?
CREATE INDEX invoice_cart_invoice_id ON nw.invoice_cart USING btree (invoice_id);


CREATE TABLE nw.party
(
    id                         bigserial                                                        NOT NULL,
    event_created_at           timestamp without time zone                                      NOT NULL,
    party_id                   character varying                                                NOT NULL,
    contact_info_email         character varying                                                NOT NULL,
    created_at                 timestamp without time zone                                      NOT NULL,
    blocking                   nw.blocking                                                      NOT NULL,
    blocking_unblocked_reason  character varying,
    blocking_unblocked_since   timestamp without time zone,
    blocking_blocked_reason    character varying,
    blocking_blocked_since     timestamp without time zone,
    suspension                 nw.suspension                                                    NOT NULL,
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

CREATE INDEX party_contact_info_email ON nw.party USING btree (contact_info_email);
CREATE INDEX party_created_at ON nw.party USING btree (created_at);
CREATE INDEX party_current ON nw.party USING btree (current);
CREATE INDEX party_event_created_at ON nw.party USING btree (event_created_at);
CREATE INDEX party_party_id ON nw.party USING btree (party_id);


CREATE TABLE nw.payment
(
    id                                               bigserial                                                        NOT NULL,
    event_created_at                                 timestamp without time zone                                      NOT NULL,
    payment_id                                       character varying                                                NOT NULL,
    created_at                                       timestamp without time zone                                      NOT NULL,
    invoice_id                                       character varying                                                NOT NULL,
    party_id                                         character varying                                                NOT NULL,
    shop_id                                          character varying                                                NOT NULL,
    domain_revision                                  bigint                                                           NOT NULL,
    party_revision                                   bigint,
    status                                           nw.payment_status                                                NOT NULL,
    status_cancelled_reason                          character varying,
    status_captured_reason                           character varying,
    status_failed_failure                            character varying,
    amount                                           bigint                                                           NOT NULL,
    currency_code                                    character varying                                                NOT NULL,
    payer_type                                       nw.payer_type                                                    NOT NULL,
    payer_payment_tool_type                          nw.payment_tool_type                                             NOT NULL,
    payer_bank_card_token                            character varying,
    payer_bank_card_payment_system                   character varying,
    payer_bank_card_bin                              character varying,
    payer_bank_card_masked_pan                       character varying,
    payer_bank_card_token_provider                   character varying,
    payer_payment_terminal_type                      character varying,
    payer_digital_wallet_provider                    character varying,
    payer_digital_wallet_id                          character varying,
    payer_payment_session_id                         character varying,
    payer_ip_address                                 character varying,
    payer_fingerprint                                character varying,
    payer_phone_number                               character varying,
    payer_email                                      character varying,
    payer_customer_id                                character varying,
    payer_customer_binding_id                        character varying,
    payer_customer_rec_payment_tool_id               character varying,
    context                                          bytea,
    payment_flow_type                                nw.payment_flow_type                                             NOT NULL,
    payment_flow_on_hold_expiration                  character varying,
    payment_flow_held_until                          timestamp without time zone,
    risk_score                                       nw.risk_score,
    route_provider_id                                integer,
    route_terminal_id                                integer,
    wtime                                            timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                          boolean                     DEFAULT true                         NOT NULL,
    session_payload_transaction_bound_trx_id         character varying,
    session_payload_transaction_bound_trx_extra_json character varying,
    fee                                              bigint,
    provider_fee                                     bigint,
    external_fee                                     bigint,
    guarantee_deposit                                bigint,
    make_recurrent                                   boolean,
    payer_recurrent_parent_invoice_id                character varying,
    payer_recurrent_parent_payment_id                character varying,
    recurrent_intention_token                        character varying,
    sequence_id                                      bigint,
    change_id                                        integer,
    trx_additional_info_rrn                          character varying,
    trx_additional_info_approval_code                character varying,
    trx_additional_info_acs_url                      character varying,
    trx_additional_info_pareq                        character varying,
    trx_additional_info_md                           character varying,
    trx_additional_info_term_url                     character varying,
    trx_additional_info_pares                        character varying,
    trx_additional_info_eci                          character varying,
    trx_additional_info_cavv                         character varying,
    trx_additional_info_xid                          character varying,
    trx_additional_info_cavv_algorithm               character varying,
    trx_additional_info_three_ds_verification        character varying,
    payer_crypto_currency_type                       character varying,
    status_captured_started_reason                   character varying,
    payer_mobile_operator_legacy                     nw.mobile_operator_type,
    payer_mobile_phone_cc                            character varying,
    payer_mobile_phone_ctn                           character varying,
    capture_started_params_cart_json                 character varying,
    external_id                                      character varying,
    payer_issuer_country                             character varying,
    payer_bank_name                                  character varying,
    payer_bank_card_cardholder_name                  character varying,
    payer_mobile_operator                            character varying,
    CONSTRAINT payment_pkey PRIMARY KEY (id),
    CONSTRAINT payment_uniq UNIQUE (invoice_id, sequence_id, change_id)
);

CREATE INDEX payment_created_at ON nw.payment USING btree (created_at);
CREATE INDEX payment_event_created_at ON nw.payment USING btree (event_created_at);
CREATE INDEX payment_external_id_idx ON nw.payment USING btree (external_id) WHERE (external_id IS NOT NULL);
CREATE INDEX payment_invoice_id ON nw.payment USING btree (invoice_id);
CREATE INDEX payment_party_id ON nw.payment USING btree (party_id);
CREATE INDEX payment_status ON nw.payment USING btree (status);

CREATE SEQUENCE nw.pmnt_seq
    START WITH 600000000
    INCREMENT BY 1
    MINVALUE 600000000
    NO MAXVALUE
    CACHE 1;


CREATE TABLE nw.payment_institution
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

CREATE INDEX payment_institution_idx ON nw.payment_institution USING btree (payment_institution_ref_id);
CREATE INDEX payment_institution_version_id ON nw.payment_institution USING btree (version_id);


CREATE TABLE nw.payment_method
(
    id                    bigserial                                                        NOT NULL,
    version_id            bigint                                                           NOT NULL,
    payment_method_ref_id character varying                                                NOT NULL,
    name                  character varying                                                NOT NULL,
    description           character varying                                                NOT NULL,
    type                  nw.payment_method_type                                           NOT NULL,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current               boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT payment_method_pkey PRIMARY KEY (id)
);

CREATE INDEX payment_method_idx ON nw.payment_method USING btree (payment_method_ref_id);
CREATE INDEX payment_method_version_id ON nw.payment_method USING btree (version_id);


CREATE TABLE nw.payment_routing_rule
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

CREATE INDEX payment_routing_rule_ref_id ON nw.payment_routing_rule USING btree (rule_ref_id);

CREATE TABLE nw.payout
(
    id                bigserial                                                        NOT NULL,
    payout_id         character varying                                                NOT NULL,
    event_created_at  timestamp without time zone                                      NOT NULL,
    sequence_id       integer                                                          NOT NULL,
    created_at        timestamp without time zone                                      NOT NULL,
    party_id          character varying                                                NOT NULL,
    shop_id           character varying                                                NOT NULL,
    status            nw.payout_status                                                 NOT NULL,
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

CREATE TABLE nw.payout_method
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

CREATE INDEX payout_method_idx ON nw.payout_method USING btree (payout_method_ref_id);
CREATE INDEX payout_method_version_id ON nw.payout_method USING btree (version_id);


CREATE TABLE nw.payout_tool
(
    id                                                             bigserial                   NOT NULL,
    cntrct_id                                                      bigint                      NOT NULL,
    payout_tool_id                                                 character varying           NOT NULL,
    created_at                                                     timestamp without time zone NOT NULL,
    currency_code                                                  character varying           NOT NULL,
    payout_tool_info                                               nw.payout_tool_info         NOT NULL,
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

CREATE INDEX payout_tool_idx ON nw.payout_tool USING btree (cntrct_id);


CREATE TABLE nw.provider
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

CREATE INDEX provider_idx ON nw.provider USING btree (provider_ref_id);
CREATE INDEX provider_version_id ON nw.provider USING btree (version_id);

CREATE TABLE nw.proxy
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

CREATE INDEX proxy_idx ON nw.proxy USING btree (proxy_ref_id);
CREATE INDEX proxy_version_id ON nw.proxy USING btree (version_id);


CREATE TABLE nw.rate
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

CREATE INDEX rate_event_created_at_idx ON nw.rate USING btree (event_created_at);
CREATE INDEX rate_source_id_idx ON nw.rate USING btree (source_id);
CREATE UNIQUE INDEX rate_ukey ON nw.rate USING btree (source_id, sequence_id, source_symbolic_code,
                                                      destination_symbolic_code);


CREATE TABLE nw.recurrent_payment_tool
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
    status                                                    nw.recurrent_payment_tool_status                                 NOT NULL,
    status_failed_failure                                     character varying,
    payment_tool_type                                         nw.payment_tool_type                                             NOT NULL,
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
    mobile_commerce_operator_legacy                           nw.mobile_operator_type,
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

CREATE INDEX recurrent_payment_tool_id_idx ON nw.recurrent_payment_tool USING btree (recurrent_payment_tool_id);

CREATE TABLE nw.refund
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
    status                                           nw.refund_status                                                 NOT NULL,
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

CREATE INDEX refund_created_at ON nw.refund USING btree (created_at);
CREATE INDEX refund_event_created_at ON nw.refund USING btree (event_created_at);
CREATE INDEX refund_external_id_idx ON nw.refund USING btree (external_id) WHERE (external_id IS NOT NULL);
CREATE INDEX refund_invoice_id ON nw.refund USING btree (invoice_id);
CREATE INDEX refund_party_id ON nw.refund USING btree (party_id);
CREATE INDEX refund_status ON nw.refund USING btree (status);


CREATE TABLE nw.shedlock
(
    name       character varying(64) NOT NULL,
    lock_until timestamp(3) without time zone,
    locked_at  timestamp(3) without time zone,
    locked_by  character varying(255),
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);

CREATE TABLE nw.shop
(
    id                         bigserial                                                        NOT NULL,
    event_created_at           timestamp without time zone                                      NOT NULL,
    party_id                   character varying                                                NOT NULL,
    shop_id                    character varying                                                NOT NULL,
    created_at                 timestamp without time zone                                      NOT NULL,
    blocking                   nw.blocking                                                      NOT NULL,
    blocking_unblocked_reason  character varying,
    blocking_unblocked_since   timestamp without time zone,
    blocking_blocked_reason    character varying,
    blocking_blocked_since     timestamp without time zone,
    suspension                 nw.suspension                                                    NOT NULL,
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

CREATE INDEX shop_created_at ON nw.shop USING btree (created_at);
CREATE INDEX shop_event_created_at ON nw.shop USING btree (event_created_at);
CREATE INDEX shop_party_id ON nw.shop USING btree (party_id);
CREATE INDEX shop_shop_id ON nw.shop USING btree (shop_id);


CREATE TABLE nw.shop_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT shop_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX shop_revision_idx ON nw.shop_revision USING btree (obj_id, revision);


CREATE TABLE nw.source
(
    id                        bigserial                                                        NOT NULL,
    event_created_at          timestamp without time zone                                      NOT NULL,
    event_occured_at          timestamp without time zone                                      NOT NULL,
    sequence_id               integer                                                          NOT NULL,
    source_id                 character varying                                                NOT NULL,
    source_name               character varying                                                NOT NULL,
    source_status             nw.source_status                                                 NOT NULL,
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

CREATE INDEX source_event_created_at_idx ON nw.source USING btree (event_created_at);
CREATE INDEX source_event_occured_at_idx ON nw.source USING btree (event_occured_at);
CREATE INDEX source_id_idx ON nw.source USING btree (source_id);


CREATE TABLE nw.term_set_hierarchy
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

CREATE INDEX term_set_hierarchy_idx ON nw.term_set_hierarchy USING btree (term_set_hierarchy_ref_id);
CREATE INDEX term_set_hierarchy_version_id ON nw.term_set_hierarchy USING btree (version_id);


CREATE TABLE nw.terminal
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

CREATE INDEX terminal_idx ON nw.terminal USING btree (terminal_ref_id);
CREATE INDEX terminal_version_id ON nw.terminal USING btree (version_id);


CREATE TABLE nw.trade_bloc
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

CREATE TABLE nw.wallet
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

CREATE INDEX wallet_event_created_at_idx ON nw.wallet USING btree (event_created_at);
CREATE INDEX wallet_event_occured_at_idx ON nw.wallet USING btree (event_occured_at);
CREATE INDEX wallet_id_idx ON nw.wallet USING btree (wallet_id);


CREATE TABLE nw.withdrawal
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
    withdrawal_status                     nw.withdrawal_status                                             NOT NULL,
    withdrawal_transfer_status            nw.withdrawal_transfer_status,
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

CREATE INDEX withdrawal_event_created_at_idx ON nw.withdrawal USING btree (event_created_at);
CREATE INDEX withdrawal_event_occured_at_idx ON nw.withdrawal USING btree (event_occured_at);
CREATE INDEX withdrawal_id_idx ON nw.withdrawal USING btree (withdrawal_id);


CREATE TABLE nw.withdrawal_provider
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

CREATE INDEX withdrawal_provider_idx ON nw.withdrawal_provider USING btree (withdrawal_provider_ref_id);
CREATE INDEX withdrawal_provider_version_id ON nw.withdrawal_provider USING btree (version_id);


CREATE TABLE nw.withdrawal_session
(
    id                                bigserial                                                        NOT NULL,
    event_created_at                  timestamp without time zone                                      NOT NULL,
    event_occured_at                  timestamp without time zone                                      NOT NULL,
    sequence_id                       integer                                                          NOT NULL,
    withdrawal_session_id             character varying                                                NOT NULL,
    withdrawal_session_status         nw.withdrawal_session_status                                     NOT NULL,
    provider_id_legacy                character varying,
    withdrawal_id                     character varying                                                NOT NULL,
    destination_card_token            character varying,
    destination_card_payment_system   nw.bank_card_payment_system,
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
    resource_type                     nw.destination_resource_type                                     NOT NULL,
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

CREATE INDEX withdrawal_session_event_created_at_idx ON nw.withdrawal_session USING btree (event_created_at);
CREATE INDEX withdrawal_session_event_occured_at_idx ON nw.withdrawal_session USING btree (event_occured_at);
CREATE INDEX withdrawal_session_id_idx ON nw.withdrawal_session USING btree (withdrawal_session_id);


-- Functions

CREATE FUNCTION nw.cashflow_sum_finalfunc(amounts bigint[]) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE STRICT
AS
$_$
begin
    return (select sum(amount_values) from unnest($1) as amount_values);
end;
$_$;

CREATE FUNCTION nw.cashflow_sum_finalfunc(amount bigint) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$$
begin
    return amount;
end;
$$;


CREATE FUNCTION nw.get_adjustment_amount_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'provider'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_amount_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'provider'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_external_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'external'::nw.cash_flow_account,
                '{"income", "outcome"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_external_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'external'::nw.cash_flow_account,
                '{"income", "outcome"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_provider_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_adjustment_provider_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'adjustment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_cashflow_sum(_cash_flow nw.cash_flow, obj_type nw.payment_change_type,
                                    source_account_type nw.cash_flow_account,
                                    source_account_type_values character varying[],
                                    destination_account_type nw.cash_flow_account,
                                    destination_account_type_values character varying[]) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return (
        coalesce(
                (
                    select amount
                    from (select ($1).*) as cash_flow
                    where cash_flow.obj_type = $2
                      and cash_flow.source_account_type = $3
                      and cash_flow.source_account_type_value = ANY ($4)
                      and cash_flow.destination_account_type = $5
                      and cash_flow.destination_account_type_value = ANY ($6)
                      and (
                            (cash_flow.obj_type = 'adjustment' and cash_flow.adj_flow_type = 'new_cash_flow')
                            or (cash_flow.obj_type != 'adjustment' and cash_flow.adj_flow_type is null)
                        )
                ), 0)
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_amount_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'provider'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_amount_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'provider'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_external_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'external'::nw.cash_flow_account,
                '{"income", "outcome"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_external_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'external'::nw.cash_flow_account,
                '{"income", "outcome"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_guarantee_deposit_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"guarantee"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_guarantee_deposit_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"guarantee"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_provider_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payment_provider_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payment'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payout_amount_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payout'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"payout"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payout_amount_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payout'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'merchant'::nw.cash_flow_account,
                '{"payout"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payout_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payout'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payout_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payout'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payout_fixed_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'payout'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"payout"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_payout_fixed_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'payout'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"payout"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_amount_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_amount_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_external_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'external'::nw.cash_flow_account,
                '{"income", "outcome"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_external_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'external'::nw.cash_flow_account,
                '{"income", "outcome"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'merchant'::nw.cash_flow_account,
                '{"settlement"}',
                'system'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_provider_fee_sfunc(amounts bigint[], cash_flow nw.cash_flow) RETURNS bigint[]
    LANGUAGE plpgsql
AS
$_$
begin
    return $1 || (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

CREATE FUNCTION nw.get_refund_provider_fee_sfunc(amount bigint, cash_flow nw.cash_flow) RETURNS bigint
    LANGUAGE plpgsql
    IMMUTABLE PARALLEL SAFE
AS
$_$
begin
    return $1 + (
        nw.get_cashflow_sum(
                $2,
                'refund'::nw.payment_change_type,
                'system'::nw.cash_flow_account,
                '{"settlement"}',
                'provider'::nw.cash_flow_account,
                '{"settlement"}'
            )
        );
end;
$_$;

-- AGGREGATES

CREATE AGGREGATE nw.get_adjustment_amount(nw.cash_flow) (
    SFUNC = nw.get_adjustment_amount_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_adjustment_external_fee(nw.cash_flow) (
    SFUNC = nw.get_adjustment_external_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_adjustment_fee(nw.cash_flow) (
    SFUNC = nw.get_adjustment_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_adjustment_provider_fee(nw.cash_flow) (
    SFUNC = nw.get_adjustment_provider_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payment_amount(nw.cash_flow) (
    SFUNC = nw.get_payment_amount_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payment_external_fee(nw.cash_flow) (
    SFUNC = nw.get_payment_external_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payment_fee(nw.cash_flow) (
    SFUNC = nw.get_payment_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payment_guarantee_deposit(nw.cash_flow) (
    SFUNC = nw.get_payment_guarantee_deposit_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payment_provider_fee(nw.cash_flow) (
    SFUNC = nw.get_payment_provider_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payout_amount(nw.cash_flow) (
    SFUNC = nw.get_payout_amount_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payout_fee(nw.cash_flow) (
    SFUNC = nw.get_payout_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_payout_fixed_fee(nw.cash_flow) (
    SFUNC = nw.get_payout_fixed_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_refund_amount(nw.cash_flow) (
    SFUNC = nw.get_refund_amount_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_refund_external_fee(nw.cash_flow) (
    SFUNC = nw.get_refund_external_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_refund_fee(nw.cash_flow) (
    SFUNC = nw.get_refund_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );

CREATE AGGREGATE nw.get_refund_provider_fee(nw.cash_flow) (
    SFUNC = nw.get_refund_provider_fee_sfunc,
    STYPE = bigint,
    INITCOND = '0',
    FINALFUNC = nw.cashflow_sum_finalfunc,
    PARALLEL = safe
    );