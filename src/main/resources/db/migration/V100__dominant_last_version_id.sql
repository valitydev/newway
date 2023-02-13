CREATE TABLE nw.dominant_last_version_id
(
    version_id      BIGINT NOT NULL,
    wtime           TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() at time zone 'utc')
);

insert into nw.dominant_last_version_id(version_id) values((with max_ids as (
select max(version_id) as id from nw.CALENDAR
union all
select max(version_id) as id from nw.CATEGORY
union all
select max(version_id) as id from nw.COUNTRY
union all
select max(version_id) as id from nw.CURRENCY
union all
select max(version_id) as id from nw.INSPECTOR
union all
select max(version_id) as id from nw.PAYMENT_INSTITUTION
union all
select max(version_id)  as id from nw.PAYMENT_METHOD
union all
select max(version_id)  as id from nw.PAYOUT_METHOD
union all
select max(version_id) as id from nw.PROVIDER
union all
select max(version_id) as id from nw.PROXY
union all
select max(version_id) as id from nw.TERMINAL
union all
select max(version_id) as id from nw.TERM_SET_HIERARCHY
union all
select max(version_id) as id from nw.TRADE_BLOC
union all
select max(version_id) as id from nw.WITHDRAWAL_PROVIDER
union all
select max(version_id) as id from nw.PAYMENT_ROUTING_RULE
union all
select max(version_id) as id from nw.CATEGORY)

select coalesce (max(id),0) from max_ids))
;

