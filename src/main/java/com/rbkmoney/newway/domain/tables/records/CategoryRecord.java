/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables.records;


import com.rbkmoney.newway.domain.tables.Category;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


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
public class CategoryRecord extends UpdatableRecordImpl<CategoryRecord> implements Record5<Long, Long, Integer, LocalDateTime, Boolean> {

    private static final long serialVersionUID = 124038482;

    /**
     * Setter for <code>nw.category.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>nw.category.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>nw.category.version_id</code>.
     */
    public void setVersionId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>nw.category.version_id</code>.
     */
    public Long getVersionId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>nw.category.category_id</code>.
     */
    public void setCategoryId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>nw.category.category_id</code>.
     */
    public Integer getCategoryId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>nw.category.wtime</code>.
     */
    public void setWtime(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>nw.category.wtime</code>.
     */
    public LocalDateTime getWtime() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>nw.category.current</code>.
     */
    public void setCurrent(Boolean value) {
        set(4, value);
    }

    /**
     * Getter for <code>nw.category.current</code>.
     */
    public Boolean getCurrent() {
        return (Boolean) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Long, Long, Integer, LocalDateTime, Boolean> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Long, Long, Integer, LocalDateTime, Boolean> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Category.CATEGORY.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Category.CATEGORY.VERSION_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return Category.CATEGORY.CATEGORY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field4() {
        return Category.CATEGORY.WTIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field5() {
        return Category.CATEGORY.CURRENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getVersionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getCategoryId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value4() {
        return getWtime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value5() {
        return getCurrent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value2(Long value) {
        setVersionId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value3(Integer value) {
        setCategoryId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value4(LocalDateTime value) {
        setWtime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value5(Boolean value) {
        setCurrent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord values(Long value1, Long value2, Integer value3, LocalDateTime value4, Boolean value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CategoryRecord
     */
    public CategoryRecord() {
        super(Category.CATEGORY);
    }

    /**
     * Create a detached, initialised CategoryRecord
     */
    public CategoryRecord(Long id, Long versionId, Integer categoryId, LocalDateTime wtime, Boolean current) {
        super(Category.CATEGORY);

        set(0, id);
        set(1, versionId);
        set(2, categoryId);
        set(3, wtime);
        set(4, current);
    }
}