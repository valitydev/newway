/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables;


import com.rbkmoney.newway.domain.Keys;
import com.rbkmoney.newway.domain.Nw;
import com.rbkmoney.newway.domain.enums.Blocking;
import com.rbkmoney.newway.domain.enums.Suspension;
import com.rbkmoney.newway.domain.tables.records.ShopRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Shop extends TableImpl<ShopRecord> {

    private static final long serialVersionUID = 171487107;

    /**
     * The reference instance of <code>nw.shop</code>
     */
    public static final Shop SHOP = new Shop();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ShopRecord> getRecordType() {
        return ShopRecord.class;
    }

    /**
     * The column <code>nw.shop.id</code>.
     */
    public final TableField<ShopRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('nw.shop_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>nw.shop.event_id</code>.
     */
    public final TableField<ShopRecord, Long> EVENT_ID = createField("event_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nw.shop.event_created_at</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> EVENT_CREATED_AT = createField("event_created_at", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.shop.party_id</code>.
     */
    public final TableField<ShopRecord, String> PARTY_ID = createField("party_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.shop.shop_id</code>.
     */
    public final TableField<ShopRecord, String> SHOP_ID = createField("shop_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.shop.created_at</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.shop.blocking</code>.
     */
    public final TableField<ShopRecord, Blocking> BLOCKING = createField("blocking", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(com.rbkmoney.newway.domain.enums.Blocking.class), this, "");

    /**
     * The column <code>nw.shop.blocking_unblocked_reason</code>.
     */
    public final TableField<ShopRecord, String> BLOCKING_UNBLOCKED_REASON = createField("blocking_unblocked_reason", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.shop.blocking_unblocked_since</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> BLOCKING_UNBLOCKED_SINCE = createField("blocking_unblocked_since", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>nw.shop.blocking_blocked_reason</code>.
     */
    public final TableField<ShopRecord, String> BLOCKING_BLOCKED_REASON = createField("blocking_blocked_reason", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.shop.blocking_blocked_since</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> BLOCKING_BLOCKED_SINCE = createField("blocking_blocked_since", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>nw.shop.suspension</code>.
     */
    public final TableField<ShopRecord, Suspension> SUSPENSION = createField("suspension", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(com.rbkmoney.newway.domain.enums.Suspension.class), this, "");

    /**
     * The column <code>nw.shop.suspension_active_since</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> SUSPENSION_ACTIVE_SINCE = createField("suspension_active_since", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>nw.shop.suspension_suspended_since</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> SUSPENSION_SUSPENDED_SINCE = createField("suspension_suspended_since", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>nw.shop.details_name</code>.
     */
    public final TableField<ShopRecord, String> DETAILS_NAME = createField("details_name", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.shop.details_description</code>.
     */
    public final TableField<ShopRecord, String> DETAILS_DESCRIPTION = createField("details_description", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.shop.location_url</code>.
     */
    public final TableField<ShopRecord, String> LOCATION_URL = createField("location_url", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.shop.category_id</code>.
     */
    public final TableField<ShopRecord, Integer> CATEGORY_ID = createField("category_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>nw.shop.account_currency_code</code>.
     */
    public final TableField<ShopRecord, String> ACCOUNT_CURRENCY_CODE = createField("account_currency_code", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.shop.account_settlement</code>.
     */
    public final TableField<ShopRecord, Long> ACCOUNT_SETTLEMENT = createField("account_settlement", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>nw.shop.account_guarantee</code>.
     */
    public final TableField<ShopRecord, Long> ACCOUNT_GUARANTEE = createField("account_guarantee", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>nw.shop.account_payout</code>.
     */
    public final TableField<ShopRecord, Long> ACCOUNT_PAYOUT = createField("account_payout", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>nw.shop.contract_id</code>.
     */
    public final TableField<ShopRecord, String> CONTRACT_ID = createField("contract_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.shop.payout_tool_id</code>.
     */
    public final TableField<ShopRecord, String> PAYOUT_TOOL_ID = createField("payout_tool_id", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.shop.payout_schedule_id</code>.
     */
    public final TableField<ShopRecord, Integer> PAYOUT_SCHEDULE_ID = createField("payout_schedule_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>nw.shop.wtime</code>.
     */
    public final TableField<ShopRecord, LocalDateTime> WTIME = createField("wtime", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>nw.shop.current</code>.
     */
    public final TableField<ShopRecord, Boolean> CURRENT = createField("current", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>nw.shop</code> table reference
     */
    public Shop() {
        this("shop", null);
    }

    /**
     * Create an aliased <code>nw.shop</code> table reference
     */
    public Shop(String alias) {
        this(alias, SHOP);
    }

    private Shop(String alias, Table<ShopRecord> aliased) {
        this(alias, aliased, null);
    }

    private Shop(String alias, Table<ShopRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Nw.NW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<ShopRecord, Long> getIdentity() {
        return Keys.IDENTITY_SHOP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ShopRecord> getPrimaryKey() {
        return Keys.SHOP_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ShopRecord>> getKeys() {
        return Arrays.<UniqueKey<ShopRecord>>asList(Keys.SHOP_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shop as(String alias) {
        return new Shop(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Shop rename(String name) {
        return new Shop(name, null);
    }
}
