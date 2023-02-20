CREATE TABLE dw.dominant_last_version_id
(
    version_id BIGINT                      NOT NULL,
    wtime      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() at time zone 'utc')
);

insert into dw.dominant_last_version_id(version_id)
values ((with max_ids as (
    select max(version_id) as id
    from dw.CALENDAR
    union all
    select max(version_id) as id
    from dw.CATEGORY
    union all
    select max(version_id) as id
    from dw.COUNTRY
    union all
    select max(version_id) as id
    from dw.CURRENCY
    union all
    select max(version_id) as id
    from dw.INSPECTOR
    union all
    select max(version_id) as id
    from dw.PAYMENT_INSTITUTION
    union all
    select max(version_id) as id
    from dw.PAYMENT_METHOD
    union all
    select max(version_id) as id
    from dw.PAYOUT_METHOD
    union all
    select max(version_id) as id
    from dw.PROVIDER
    union all
    select max(version_id) as id
    from dw.PROXY
    union all
    select max(version_id) as id
    from dw.TERMINAL
    union all
    select max(version_id) as id
    from dw.TERM_SET_HIERARCHY
    union all
    select max(version_id) as id
    from dw.TRADE_BLOC
    union all
    select max(version_id) as id
    from dw.WITHDRAWAL_PROVIDER
    union all
    select max(version_id) as id
    from dw.PAYMENT_ROUTING_RULE
    union all
    select max(version_id) as id
    from dw.CATEGORY)
         select coalesce(max(id), 0)
         from max_ids))
;