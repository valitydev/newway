/*
 * This file is generated by jOOQ.
 */
package com.rbkmoney.newway.domain.tables;


import com.rbkmoney.newway.domain.Indexes;
import com.rbkmoney.newway.domain.Keys;
import com.rbkmoney.newway.domain.Nw;
import com.rbkmoney.newway.domain.tables.records.CategoryRecord;

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
public class Category extends TableImpl<CategoryRecord> {

    private static final long serialVersionUID = 765499860;

    /**
     * The reference instance of <code>nw.category</code>
     */
    public static final Category CATEGORY = new Category();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CategoryRecord> getRecordType() {
        return CategoryRecord.class;
    }

    /**
     * The column <code>nw.category.id</code>.
     */
    public final TableField<CategoryRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('nw.category_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>nw.category.version_id</code>.
     */
    public final TableField<CategoryRecord, Long> VERSION_ID = createField("version_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>nw.category.category_id</code>.
     */
    public final TableField<CategoryRecord, Integer> CATEGORY_ID = createField("category_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>nw.category.wtime</code>.
     */
    public final TableField<CategoryRecord, LocalDateTime> WTIME = createField("wtime", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("timezone('utc'::text, now())", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>nw.category.current</code>.
     */
    public final TableField<CategoryRecord, Boolean> CURRENT = createField("current", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>nw.category</code> table reference
     */
    public Category() {
        this(DSL.name("category"), null);
    }

    /**
     * Create an aliased <code>nw.category</code> table reference
     */
    public Category(String alias) {
        this(DSL.name(alias), CATEGORY);
    }

    /**
     * Create an aliased <code>nw.category</code> table reference
     */
    public Category(Name alias) {
        this(alias, CATEGORY);
    }

    private Category(Name alias, Table<CategoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private Category(Name alias, Table<CategoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Category(Table<O> child, ForeignKey<O, CategoryRecord> key) {
        super(child, key, CATEGORY);
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
        return Arrays.<Index>asList(Indexes.CATEGORY_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CategoryRecord, Long> getIdentity() {
        return Keys.IDENTITY_CATEGORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CategoryRecord> getPrimaryKey() {
        return Keys.CATEGORY_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CategoryRecord>> getKeys() {
        return Arrays.<UniqueKey<CategoryRecord>>asList(Keys.CATEGORY_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category as(String alias) {
        return new Category(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category as(Name alias) {
        return new Category(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Category rename(String name) {
        return new Category(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Category rename(Name name) {
        return new Category(name, null);
    }
}
