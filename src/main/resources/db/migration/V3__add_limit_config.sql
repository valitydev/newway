create type dw.limit_config_time_range_type as enum ('calendar', 'interval');
create type dw.limit_config_time_range_type_calendar as enum ('year', 'month', 'week', 'day');
create type dw.limit_config_limit_context_type as enum ('payment_processing', 'withdrawal_processing');
create type dw.limit_config_limit_type_turnover_metric as enum ('number', 'amount');
create type dw.limit_config_limit_scope as enum ('multi', 'single');
create type dw.limit_config_limit_scope_type as enum ('party', 'shop', 'wallet', 'identity', 'payment_tool');
create type dw.limit_config_operation_limit_behaviour as enum ('subtraction', 'addition');

create table if not exists dw.limit_config
(
    id                                         bigserial                          not null,
    source_id                                  varchar                            not null,
    sequence_id                                int                                not null,
    event_occured_at                           timestamp                          not null,
    event_created_at                           timestamp                          not null,
    limit_config_id                            varchar                            not null,
    processor_type                             varchar                            not null,
    created_at                                 timestamp                          not null,
    started_at                                 timestamp                          not null,
    shard_size                                 bigint                             not null,
    time_range_type                            dw.limit_config_time_range_type    not null,
    time_range_type_calendar                   dw.limit_config_time_range_type_calendar,
    time_range_type_interval_amount            bigint,
    limit_context_type                         dw.limit_config_limit_context_type not null,
    limit_type_turnover_metric                 dw.limit_config_limit_type_turnover_metric,
    limit_type_turnover_metric_amount_currency varchar,
    limit_scope                                dw.limit_config_limit_scope,
    limit_scope_types_json                     text,
    description                                varchar,
    operation_limit_behaviour                  dw.limit_config_operation_limit_behaviour,
    wtime                                      TIMESTAMP WITHOUT TIME ZONE        NOT NULL DEFAULT (now() at time zone 'utc'),
    current                                    BOOLEAN                            NOT NULL DEFAULT TRUE,
    constraint limit_config_id_pkey primary key (id),
    constraint limit_config_limit_config_id_ukey unique (limit_config_id, sequence_id)
);