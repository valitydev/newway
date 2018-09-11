/*
 * This file is generated by jOOQ.
 */
package com.rbkmoney.newway.domain.tables;


import com.rbkmoney.newway.domain.Indexes;
import com.rbkmoney.newway.domain.Keys;
import com.rbkmoney.newway.domain.Nw;
import com.rbkmoney.newway.domain.enums.InvoiceStatus;
import com.rbkmoney.newway.domain.tables.records.InvoiceRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Invoice extends TableImpl<InvoiceRecord> {

    private static final long serialVersionUID = 2049951368;

    /**
     * The reference instance of <code>nw.invoice</code>
     */
    public static final Invoice INVOICE = new Invoice();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InvoiceRecord> getRecordType() {
        return InvoiceRecord.class;
    }

    /**
     * The column <code>nw.invoice.id</code>.
     */
    public final TableField<InvoiceRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('nw.invoice_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>nw.invoice.event_id</code>.
     */
    public final TableField<InvoiceRecord, Long> EVENT_ID = createField("event_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.event_created_at</code>.
     */
    public final TableField<InvoiceRecord, LocalDateTime> EVENT_CREATED_AT = createField("event_created_at", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.invoice_id</code>.
     */
    public final TableField<InvoiceRecord, String> INVOICE_ID = createField("invoice_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.party_id</code>.
     */
    public final TableField<InvoiceRecord, String> PARTY_ID = createField("party_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.shop_id</code>.
     */
    public final TableField<InvoiceRecord, String> SHOP_ID = createField("shop_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.party_revision</code>.
     */
    public final TableField<InvoiceRecord, Long> PARTY_REVISION = createField("party_revision", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>nw.invoice.created_at</code>.
     */
    public final TableField<InvoiceRecord, LocalDateTime> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.status</code>.
     */
    public final TableField<InvoiceRecord, InvoiceStatus> STATUS = createField("status", org.jooq.impl.SQLDataType.VARCHAR.nullable(false).asEnumDataType(com.rbkmoney.newway.domain.enums.InvoiceStatus.class), this, "");

    /**
     * The column <code>nw.invoice.status_cancelled_details</code>.
     */
    public final TableField<InvoiceRecord, String> STATUS_CANCELLED_DETAILS = createField("status_cancelled_details", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.invoice.status_fulfilled_details</code>.
     */
    public final TableField<InvoiceRecord, String> STATUS_FULFILLED_DETAILS = createField("status_fulfilled_details", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.invoice.details_product</code>.
     */
    public final TableField<InvoiceRecord, String> DETAILS_PRODUCT = createField("details_product", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.details_description</code>.
     */
    public final TableField<InvoiceRecord, String> DETAILS_DESCRIPTION = createField("details_description", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.invoice.due</code>.
     */
    public final TableField<InvoiceRecord, LocalDateTime> DUE = createField("due", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.amount</code>.
     */
    public final TableField<InvoiceRecord, Long> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.currency_code</code>.
     */
    public final TableField<InvoiceRecord, String> CURRENCY_CODE = createField("currency_code", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.invoice.context</code>.
     */
    public final TableField<InvoiceRecord, byte[]> CONTEXT = createField("context", org.jooq.impl.SQLDataType.BLOB, this, "");

    /**
     * The column <code>nw.invoice.template_id</code>.
     */
    public final TableField<InvoiceRecord, String> TEMPLATE_ID = createField("template_id", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.invoice.wtime</code>.
     */
    public final TableField<InvoiceRecord, LocalDateTime> WTIME = createField("wtime", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("timezone('utc'::text, now())", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>nw.invoice.current</code>.
     */
    public final TableField<InvoiceRecord, Boolean> CURRENT = createField("current", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>nw.invoice</code> table reference
     */
    public Invoice() {
        this(DSL.name("invoice"), null);
    }

    /**
     * Create an aliased <code>nw.invoice</code> table reference
     */
    public Invoice(String alias) {
        this(DSL.name(alias), INVOICE);
    }

    /**
     * Create an aliased <code>nw.invoice</code> table reference
     */
    public Invoice(Name alias) {
        this(alias, INVOICE);
    }

    private Invoice(Name alias, Table<InvoiceRecord> aliased) {
        this(alias, aliased, null);
    }

    private Invoice(Name alias, Table<InvoiceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Invoice(Table<O> child, ForeignKey<O, InvoiceRecord> key) {
        super(child, key, INVOICE);
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
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.INVOICE_CREATED_AT, Indexes.INVOICE_EVENT_CREATED_AT, Indexes.INVOICE_EVENT_ID, Indexes.INVOICE_INVOICE_ID, Indexes.INVOICE_PARTY_ID, Indexes.INVOICE_PKEY, Indexes.INVOICE_STATUS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<InvoiceRecord, Long> getIdentity() {
        return Keys.IDENTITY_INVOICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<InvoiceRecord> getPrimaryKey() {
        return Keys.INVOICE_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<InvoiceRecord>> getKeys() {
        return Arrays.<UniqueKey<InvoiceRecord>>asList(Keys.INVOICE_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice as(String alias) {
        return new Invoice(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice as(Name alias) {
        return new Invoice(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(String name) {
        return new Invoice(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(Name name) {
        return new Invoice(name, null);
    }
}
