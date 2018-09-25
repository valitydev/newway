/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables;


import com.rbkmoney.newway.domain.Keys;
import com.rbkmoney.newway.domain.Nw;
import com.rbkmoney.newway.domain.tables.records.PaymentInstitutionRecord;

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
public class PaymentInstitution extends TableImpl<PaymentInstitutionRecord> {

    private static final long serialVersionUID = 749356052;

    /**
     * The reference instance of <code>nw.payment_institution</code>
     */
    public static final PaymentInstitution PAYMENT_INSTITUTION = new PaymentInstitution();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PaymentInstitutionRecord> getRecordType() {
        return PaymentInstitutionRecord.class;
    }

    /**
     * The column <code>nw.payment_institution.id</code>.
     */
    public final TableField<PaymentInstitutionRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('nw.payment_institution_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>nw.payment_institution.version_id</code>.
     */
    public final TableField<PaymentInstitutionRecord, Long> VERSION_ID = createField("version_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.payment_institution_ref_id</code>.
     */
    public final TableField<PaymentInstitutionRecord, Integer> PAYMENT_INSTITUTION_REF_ID = createField("payment_institution_ref_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.name</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.description</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.payment_institution.calendar_ref_id</code>.
     */
    public final TableField<PaymentInstitutionRecord, Integer> CALENDAR_REF_ID = createField("calendar_ref_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>nw.payment_institution.system_account_set_json</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> SYSTEM_ACCOUNT_SET_JSON = createField("system_account_set_json", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.default_contract_template_json</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> DEFAULT_CONTRACT_TEMPLATE_JSON = createField("default_contract_template_json", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.default_wallet_contract_template_json</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> DEFAULT_WALLET_CONTRACT_TEMPLATE_JSON = createField("default_wallet_contract_template_json", org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>nw.payment_institution.providers_json</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> PROVIDERS_JSON = createField("providers_json", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.inspector_json</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> INSPECTOR_JSON = createField("inspector_json", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.realm</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> REALM = createField("realm", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.residences_json</code>.
     */
    public final TableField<PaymentInstitutionRecord, String> RESIDENCES_JSON = createField("residences_json", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>nw.payment_institution.wtime</code>.
     */
    public final TableField<PaymentInstitutionRecord, LocalDateTime> WTIME = createField("wtime", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("timezone('utc'::text, now())", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>nw.payment_institution.current</code>.
     */
    public final TableField<PaymentInstitutionRecord, Boolean> CURRENT = createField("current", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>nw.payment_institution</code> table reference
     */
    public PaymentInstitution() {
        this("payment_institution", null);
    }

    /**
     * Create an aliased <code>nw.payment_institution</code> table reference
     */
    public PaymentInstitution(String alias) {
        this(alias, PAYMENT_INSTITUTION);
    }

    private PaymentInstitution(String alias, Table<PaymentInstitutionRecord> aliased) {
        this(alias, aliased, null);
    }

    private PaymentInstitution(String alias, Table<PaymentInstitutionRecord> aliased, Field<?>[] parameters) {
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
    public Identity<PaymentInstitutionRecord, Long> getIdentity() {
        return Keys.IDENTITY_PAYMENT_INSTITUTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PaymentInstitutionRecord> getPrimaryKey() {
        return Keys.PAYMENT_INSTITUTION_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PaymentInstitutionRecord>> getKeys() {
        return Arrays.<UniqueKey<PaymentInstitutionRecord>>asList(Keys.PAYMENT_INSTITUTION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentInstitution as(String alias) {
        return new PaymentInstitution(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PaymentInstitution rename(String name) {
        return new PaymentInstitution(name, null);
    }
}