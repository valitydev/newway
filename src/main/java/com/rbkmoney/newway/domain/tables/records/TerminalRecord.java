/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables.records;


import com.rbkmoney.newway.domain.tables.Terminal;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
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
public class TerminalRecord extends UpdatableRecordImpl<TerminalRecord> implements Record10<Long, Long, Integer, String, String, String, String, String, LocalDateTime, Boolean> {

    private static final long serialVersionUID = 174090348;

    /**
     * Setter for <code>nw.terminal.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>nw.terminal.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>nw.terminal.version_id</code>.
     */
    public void setVersionId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>nw.terminal.version_id</code>.
     */
    public Long getVersionId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>nw.terminal.terminal_ref_id</code>.
     */
    public void setTerminalRefId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>nw.terminal.terminal_ref_id</code>.
     */
    public Integer getTerminalRefId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>nw.terminal.name</code>.
     */
    public void setName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>nw.terminal.name</code>.
     */
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>nw.terminal.description</code>.
     */
    public void setDescription(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>nw.terminal.description</code>.
     */
    public String getDescription() {
        return (String) get(4);
    }

    /**
     * Setter for <code>nw.terminal.options_json</code>.
     */
    public void setOptionsJson(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>nw.terminal.options_json</code>.
     */
    public String getOptionsJson() {
        return (String) get(5);
    }

    /**
     * Setter for <code>nw.terminal.risk_coverage</code>.
     */
    public void setRiskCoverage(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>nw.terminal.risk_coverage</code>.
     */
    public String getRiskCoverage() {
        return (String) get(6);
    }

    /**
     * Setter for <code>nw.terminal.terms_json</code>.
     */
    public void setTermsJson(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>nw.terminal.terms_json</code>.
     */
    public String getTermsJson() {
        return (String) get(7);
    }

    /**
     * Setter for <code>nw.terminal.wtime</code>.
     */
    public void setWtime(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>nw.terminal.wtime</code>.
     */
    public LocalDateTime getWtime() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>nw.terminal.current</code>.
     */
    public void setCurrent(Boolean value) {
        set(9, value);
    }

    /**
     * Getter for <code>nw.terminal.current</code>.
     */
    public Boolean getCurrent() {
        return (Boolean) get(9);
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
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Integer, String, String, String, String, String, LocalDateTime, Boolean> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Integer, String, String, String, String, String, LocalDateTime, Boolean> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Terminal.TERMINAL.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Terminal.TERMINAL.VERSION_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return Terminal.TERMINAL.TERMINAL_REF_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Terminal.TERMINAL.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Terminal.TERMINAL.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Terminal.TERMINAL.OPTIONS_JSON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Terminal.TERMINAL.RISK_COVERAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Terminal.TERMINAL.TERMS_JSON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field9() {
        return Terminal.TERMINAL.WTIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field10() {
        return Terminal.TERMINAL.CURRENT;
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
        return getTerminalRefId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getOptionsJson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getRiskCoverage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getTermsJson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value9() {
        return getWtime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value10() {
        return getCurrent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value2(Long value) {
        setVersionId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value3(Integer value) {
        setTerminalRefId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value4(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value5(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value6(String value) {
        setOptionsJson(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value7(String value) {
        setRiskCoverage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value8(String value) {
        setTermsJson(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value9(LocalDateTime value) {
        setWtime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord value10(Boolean value) {
        setCurrent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TerminalRecord values(Long value1, Long value2, Integer value3, String value4, String value5, String value6, String value7, String value8, LocalDateTime value9, Boolean value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TerminalRecord
     */
    public TerminalRecord() {
        super(Terminal.TERMINAL);
    }

    /**
     * Create a detached, initialised TerminalRecord
     */
    public TerminalRecord(Long id, Long versionId, Integer terminalRefId, String name, String description, String optionsJson, String riskCoverage, String termsJson, LocalDateTime wtime, Boolean current) {
        super(Terminal.TERMINAL);

        set(0, id);
        set(1, versionId);
        set(2, terminalRefId);
        set(3, name);
        set(4, description);
        set(5, optionsJson);
        set(6, riskCoverage);
        set(7, termsJson);
        set(8, wtime);
        set(9, current);
    }
}
