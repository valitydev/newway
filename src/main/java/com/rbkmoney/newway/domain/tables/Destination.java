/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables;


import com.rbkmoney.newway.domain.Keys;
import com.rbkmoney.newway.domain.Nw;
import com.rbkmoney.newway.domain.enums.DestinationStatus;
import com.rbkmoney.newway.domain.tables.records.DestinationRecord;

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
public class Destination extends TableImpl<DestinationRecord> {

    private static final long serialVersionUID = -1528346694;

    /**
     * The reference instance of <code>nw.destination</code>
     */
    public static final Destination DESTINATION = new Destination();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DestinationRecord> getRecordType() {
        return DestinationRecord.class;
    }

    /**
     * The column <code>nw.destination.id</code>.
     */
    public final TableField<DestinationRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('nw.destination_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>nw.destination.event_id</code>.
     */
    public final TableField<DestinationRecord, Long> EVENT_ID = createField("event_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nw.destination.event_created_at</code>.
     */
    public final TableField<DestinationRecord, LocalDateTime> EVENT_CREATED_AT = createField("event_created_at", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.destination.event_occured_at</code>.
     */
    public final TableField<DestinationRecord, LocalDateTime> EVENT_OCCURED_AT = createField("event_occured_at", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.destination.sequence_id</code>.
     */
    public final TableField<DestinationRecord, Integer> SEQUENCE_ID = createField("sequence_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>nw.destination.destination_id</code>.
     */
    public final TableField<DestinationRecord, String> DESTINATION_ID = createField("destination_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.destination.destination_name</code>.
     */
    public final TableField<DestinationRecord, String> DESTINATION_NAME = createField("destination_name", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.destination.destination_status</code>.
     */
    public final TableField<DestinationRecord, DestinationStatus> DESTINATION_STATUS = createField("destination_status", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(com.rbkmoney.newway.domain.enums.DestinationStatus.class), this, "");

    /**
     * The column <code>nw.destination.resource_bank_card_token</code>.
     */
    public final TableField<DestinationRecord, String> RESOURCE_BANK_CARD_TOKEN = createField("resource_bank_card_token", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.destination.resource_bank_card_payment_system</code>.
     */
    public final TableField<DestinationRecord, String> RESOURCE_BANK_CARD_PAYMENT_SYSTEM = createField("resource_bank_card_payment_system", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.resource_bank_card_bin</code>.
     */
    public final TableField<DestinationRecord, String> RESOURCE_BANK_CARD_BIN = createField("resource_bank_card_bin", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.resource_bank_card_masked_pan</code>.
     */
    public final TableField<DestinationRecord, String> RESOURCE_BANK_CARD_MASKED_PAN = createField("resource_bank_card_masked_pan", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.account_id</code>.
     */
    public final TableField<DestinationRecord, String> ACCOUNT_ID = createField("account_id", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.identity_id</code>.
     */
    public final TableField<DestinationRecord, String> IDENTITY_ID = createField("identity_id", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.party_id</code>.
     */
    public final TableField<DestinationRecord, String> PARTY_ID = createField("party_id", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.accounter_account_id</code>.
     */
    public final TableField<DestinationRecord, Long> ACCOUNTER_ACCOUNT_ID = createField("accounter_account_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>nw.destination.currency_code</code>.
     */
    public final TableField<DestinationRecord, String> CURRENCY_CODE = createField("currency_code", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.destination.wtime</code>.
     */
    public final TableField<DestinationRecord, LocalDateTime> WTIME = createField("wtime", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("timezone('utc'::text, now())", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>nw.destination.current</code>.
     */
    public final TableField<DestinationRecord, Boolean> CURRENT = createField("current", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>nw.destination</code> table reference
     */
    public Destination() {
        this("destination", null);
    }

    /**
     * Create an aliased <code>nw.destination</code> table reference
     */
    public Destination(String alias) {
        this(alias, DESTINATION);
    }

    private Destination(String alias, Table<DestinationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Destination(String alias, Table<DestinationRecord> aliased, Field<?>[] parameters) {
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
    public Identity<DestinationRecord, Long> getIdentity() {
        return Keys.IDENTITY_DESTINATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<DestinationRecord> getPrimaryKey() {
        return Keys.DESTINATION_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DestinationRecord>> getKeys() {
        return Arrays.<UniqueKey<DestinationRecord>>asList(Keys.DESTINATION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination as(String alias) {
        return new Destination(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Destination rename(String name) {
        return new Destination(name, null);
    }
}