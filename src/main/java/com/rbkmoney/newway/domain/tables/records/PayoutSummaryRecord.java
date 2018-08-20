/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables.records;


import com.rbkmoney.newway.domain.tables.PayoutSummary;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
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
public class PayoutSummaryRecord extends UpdatableRecordImpl<PayoutSummaryRecord> implements Record9<Long, Long, Long, Long, String, LocalDateTime, LocalDateTime, String, Integer> {

    private static final long serialVersionUID = -680742916;

    /**
     * Setter for <code>nw.payout_summary.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>nw.payout_summary.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>nw.payout_summary.pyt_id</code>.
     */
    public void setPytId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>nw.payout_summary.pyt_id</code>.
     */
    public Long getPytId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>nw.payout_summary.amount</code>.
     */
    public void setAmount(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>nw.payout_summary.amount</code>.
     */
    public Long getAmount() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>nw.payout_summary.fee</code>.
     */
    public void setFee(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>nw.payout_summary.fee</code>.
     */
    public Long getFee() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>nw.payout_summary.currency_code</code>.
     */
    public void setCurrencyCode(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>nw.payout_summary.currency_code</code>.
     */
    public String getCurrencyCode() {
        return (String) get(4);
    }

    /**
     * Setter for <code>nw.payout_summary.from_time</code>.
     */
    public void setFromTime(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>nw.payout_summary.from_time</code>.
     */
    public LocalDateTime getFromTime() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>nw.payout_summary.to_time</code>.
     */
    public void setToTime(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>nw.payout_summary.to_time</code>.
     */
    public LocalDateTime getToTime() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>nw.payout_summary.operation_type</code>.
     */
    public void setOperationType(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>nw.payout_summary.operation_type</code>.
     */
    public String getOperationType() {
        return (String) get(7);
    }

    /**
     * Setter for <code>nw.payout_summary.count</code>.
     */
    public void setCount(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>nw.payout_summary.count</code>.
     */
    public Integer getCount() {
        return (Integer) get(8);
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
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, Long, Long, Long, String, LocalDateTime, LocalDateTime, String, Integer> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, Long, Long, Long, String, LocalDateTime, LocalDateTime, String, Integer> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return PayoutSummary.PAYOUT_SUMMARY.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return PayoutSummary.PAYOUT_SUMMARY.PYT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return PayoutSummary.PAYOUT_SUMMARY.AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return PayoutSummary.PAYOUT_SUMMARY.FEE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return PayoutSummary.PAYOUT_SUMMARY.CURRENCY_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field6() {
        return PayoutSummary.PAYOUT_SUMMARY.FROM_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field7() {
        return PayoutSummary.PAYOUT_SUMMARY.TO_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return PayoutSummary.PAYOUT_SUMMARY.OPERATION_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return PayoutSummary.PAYOUT_SUMMARY.COUNT;
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
        return getPytId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getFee();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getCurrencyCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value6() {
        return getFromTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value7() {
        return getToTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getOperationType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value2(Long value) {
        setPytId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value3(Long value) {
        setAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value4(Long value) {
        setFee(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value5(String value) {
        setCurrencyCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value6(LocalDateTime value) {
        setFromTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value7(LocalDateTime value) {
        setToTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value8(String value) {
        setOperationType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord value9(Integer value) {
        setCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutSummaryRecord values(Long value1, Long value2, Long value3, Long value4, String value5, LocalDateTime value6, LocalDateTime value7, String value8, Integer value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PayoutSummaryRecord
     */
    public PayoutSummaryRecord() {
        super(PayoutSummary.PAYOUT_SUMMARY);
    }

    /**
     * Create a detached, initialised PayoutSummaryRecord
     */
    public PayoutSummaryRecord(Long id, Long pytId, Long amount, Long fee, String currencyCode, LocalDateTime fromTime, LocalDateTime toTime, String operationType, Integer count) {
        super(PayoutSummary.PAYOUT_SUMMARY);

        set(0, id);
        set(1, pytId);
        set(2, amount);
        set(3, fee);
        set(4, currencyCode);
        set(5, fromTime);
        set(6, toTime);
        set(7, operationType);
        set(8, count);
    }
}
