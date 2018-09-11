/*
 * This file is generated by jOOQ.
 */
package com.rbkmoney.newway.domain.tables.records;


import com.rbkmoney.newway.domain.enums.RefundStatus;
import com.rbkmoney.newway.domain.tables.Refund;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record17;
import org.jooq.Row17;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RefundRecord extends UpdatableRecordImpl<RefundRecord> implements Record17<Long, Long, LocalDateTime, Long, String, String, String, String, String, LocalDateTime, RefundStatus, String, Long, String, String, LocalDateTime, Boolean> {

    private static final long serialVersionUID = -794780695;

    /**
     * Setter for <code>nw.refund.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>nw.refund.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>nw.refund.event_id</code>.
     */
    public void setEventId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>nw.refund.event_id</code>.
     */
    public Long getEventId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>nw.refund.event_created_at</code>.
     */
    public void setEventCreatedAt(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>nw.refund.event_created_at</code>.
     */
    public LocalDateTime getEventCreatedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>nw.refund.domain_revision</code>.
     */
    public void setDomainRevision(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>nw.refund.domain_revision</code>.
     */
    public Long getDomainRevision() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>nw.refund.refund_id</code>.
     */
    public void setRefundId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>nw.refund.refund_id</code>.
     */
    public String getRefundId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>nw.refund.payment_id</code>.
     */
    public void setPaymentId(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>nw.refund.payment_id</code>.
     */
    public String getPaymentId() {
        return (String) get(5);
    }

    /**
     * Setter for <code>nw.refund.invoice_id</code>.
     */
    public void setInvoiceId(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>nw.refund.invoice_id</code>.
     */
    public String getInvoiceId() {
        return (String) get(6);
    }

    /**
     * Setter for <code>nw.refund.party_id</code>.
     */
    public void setPartyId(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>nw.refund.party_id</code>.
     */
    public String getPartyId() {
        return (String) get(7);
    }

    /**
     * Setter for <code>nw.refund.shop_id</code>.
     */
    public void setShopId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>nw.refund.shop_id</code>.
     */
    public String getShopId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>nw.refund.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(9, value);
    }

    /**
     * Getter for <code>nw.refund.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(9);
    }

    /**
     * Setter for <code>nw.refund.status</code>.
     */
    public void setStatus(RefundStatus value) {
        set(10, value);
    }

    /**
     * Getter for <code>nw.refund.status</code>.
     */
    public RefundStatus getStatus() {
        return (RefundStatus) get(10);
    }

    /**
     * Setter for <code>nw.refund.status_failed_failure</code>.
     */
    public void setStatusFailedFailure(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>nw.refund.status_failed_failure</code>.
     */
    public String getStatusFailedFailure() {
        return (String) get(11);
    }

    /**
     * Setter for <code>nw.refund.amount</code>.
     */
    public void setAmount(Long value) {
        set(12, value);
    }

    /**
     * Getter for <code>nw.refund.amount</code>.
     */
    public Long getAmount() {
        return (Long) get(12);
    }

    /**
     * Setter for <code>nw.refund.currency_code</code>.
     */
    public void setCurrencyCode(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>nw.refund.currency_code</code>.
     */
    public String getCurrencyCode() {
        return (String) get(13);
    }

    /**
     * Setter for <code>nw.refund.reason</code>.
     */
    public void setReason(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>nw.refund.reason</code>.
     */
    public String getReason() {
        return (String) get(14);
    }

    /**
     * Setter for <code>nw.refund.wtime</code>.
     */
    public void setWtime(LocalDateTime value) {
        set(15, value);
    }

    /**
     * Getter for <code>nw.refund.wtime</code>.
     */
    public LocalDateTime getWtime() {
        return (LocalDateTime) get(15);
    }

    /**
     * Setter for <code>nw.refund.current</code>.
     */
    public void setCurrent(Boolean value) {
        set(16, value);
    }

    /**
     * Getter for <code>nw.refund.current</code>.
     */
    public Boolean getCurrent() {
        return (Boolean) get(16);
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
    // Record17 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row17<Long, Long, LocalDateTime, Long, String, String, String, String, String, LocalDateTime, RefundStatus, String, Long, String, String, LocalDateTime, Boolean> fieldsRow() {
        return (Row17) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row17<Long, Long, LocalDateTime, Long, String, String, String, String, String, LocalDateTime, RefundStatus, String, Long, String, String, LocalDateTime, Boolean> valuesRow() {
        return (Row17) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Refund.REFUND.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Refund.REFUND.EVENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field3() {
        return Refund.REFUND.EVENT_CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return Refund.REFUND.DOMAIN_REVISION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Refund.REFUND.REFUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Refund.REFUND.PAYMENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Refund.REFUND.INVOICE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Refund.REFUND.PARTY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Refund.REFUND.SHOP_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field10() {
        return Refund.REFUND.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<RefundStatus> field11() {
        return Refund.REFUND.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return Refund.REFUND.STATUS_FAILED_FAILURE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field13() {
        return Refund.REFUND.AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return Refund.REFUND.CURRENCY_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field15() {
        return Refund.REFUND.REASON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field16() {
        return Refund.REFUND.WTIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field17() {
        return Refund.REFUND.CURRENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component2() {
        return getEventId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component3() {
        return getEventCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getDomainRevision();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getRefundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getPaymentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getInvoiceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getPartyId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getShopId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component10() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundStatus component11() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getStatusFailedFailure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component13() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component14() {
        return getCurrencyCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component15() {
        return getReason();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component16() {
        return getWtime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component17() {
        return getCurrent();
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
        return getEventId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value3() {
        return getEventCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getDomainRevision();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getRefundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getPaymentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getInvoiceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getPartyId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getShopId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value10() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundStatus value11() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getStatusFailedFailure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value13() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value14() {
        return getCurrencyCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value15() {
        return getReason();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value16() {
        return getWtime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value17() {
        return getCurrent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value2(Long value) {
        setEventId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value3(LocalDateTime value) {
        setEventCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value4(Long value) {
        setDomainRevision(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value5(String value) {
        setRefundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value6(String value) {
        setPaymentId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value7(String value) {
        setInvoiceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value8(String value) {
        setPartyId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value9(String value) {
        setShopId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value10(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value11(RefundStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value12(String value) {
        setStatusFailedFailure(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value13(Long value) {
        setAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value14(String value) {
        setCurrencyCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value15(String value) {
        setReason(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value16(LocalDateTime value) {
        setWtime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord value17(Boolean value) {
        setCurrent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefundRecord values(Long value1, Long value2, LocalDateTime value3, Long value4, String value5, String value6, String value7, String value8, String value9, LocalDateTime value10, RefundStatus value11, String value12, Long value13, String value14, String value15, LocalDateTime value16, Boolean value17) {
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
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RefundRecord
     */
    public RefundRecord() {
        super(Refund.REFUND);
    }

    /**
     * Create a detached, initialised RefundRecord
     */
    public RefundRecord(Long id, Long eventId, LocalDateTime eventCreatedAt, Long domainRevision, String refundId, String paymentId, String invoiceId, String partyId, String shopId, LocalDateTime createdAt, RefundStatus status, String statusFailedFailure, Long amount, String currencyCode, String reason, LocalDateTime wtime, Boolean current) {
        super(Refund.REFUND);

        set(0, id);
        set(1, eventId);
        set(2, eventCreatedAt);
        set(3, domainRevision);
        set(4, refundId);
        set(5, paymentId);
        set(6, invoiceId);
        set(7, partyId);
        set(8, shopId);
        set(9, createdAt);
        set(10, status);
        set(11, statusFailedFailure);
        set(12, amount);
        set(13, currencyCode);
        set(14, reason);
        set(15, wtime);
        set(16, current);
    }
}
