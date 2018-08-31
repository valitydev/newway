/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables.records;


import com.rbkmoney.newway.domain.enums.PayoutToolInfo;
import com.rbkmoney.newway.domain.tables.PayoutTool;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record16;
import org.jooq.Row16;
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
public class PayoutToolRecord extends UpdatableRecordImpl<PayoutToolRecord> implements Record16<Long, Long, String, LocalDateTime, String, PayoutToolInfo, String, String, String, String, String, String, String, String, String, String> {

    private static final long serialVersionUID = 320567426;

    /**
     * Setter for <code>nw.payout_tool.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>nw.payout_tool.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>nw.payout_tool.cntrct_id</code>.
     */
    public void setCntrctId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>nw.payout_tool.cntrct_id</code>.
     */
    public Long getCntrctId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_id</code>.
     */
    public void setPayoutToolId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_id</code>.
     */
    public String getPayoutToolId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>nw.payout_tool.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>nw.payout_tool.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>nw.payout_tool.currency_code</code>.
     */
    public void setCurrencyCode(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>nw.payout_tool.currency_code</code>.
     */
    public String getCurrencyCode() {
        return (String) get(4);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info</code>.
     */
    public void setPayoutToolInfo(PayoutToolInfo value) {
        set(5, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info</code>.
     */
    public PayoutToolInfo getPayoutToolInfo() {
        return (PayoutToolInfo) get(5);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_russian_bank_account</code>.
     */
    public void setPayoutToolInfoRussianBankAccount(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_russian_bank_account</code>.
     */
    public String getPayoutToolInfoRussianBankAccount() {
        return (String) get(6);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_russian_bank_name</code>.
     */
    public void setPayoutToolInfoRussianBankName(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_russian_bank_name</code>.
     */
    public String getPayoutToolInfoRussianBankName() {
        return (String) get(7);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_russian_bank_post_account</code>.
     */
    public void setPayoutToolInfoRussianBankPostAccount(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_russian_bank_post_account</code>.
     */
    public String getPayoutToolInfoRussianBankPostAccount() {
        return (String) get(8);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_russian_bank_bik</code>.
     */
    public void setPayoutToolInfoRussianBankBik(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_russian_bank_bik</code>.
     */
    public String getPayoutToolInfoRussianBankBik() {
        return (String) get(9);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_international_bank_account_holder</code>.
     */
    public void setPayoutToolInfoInternationalBankAccountHolder(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_international_bank_account_holder</code>.
     */
    public String getPayoutToolInfoInternationalBankAccountHolder() {
        return (String) get(10);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_international_bank_name</code>.
     */
    public void setPayoutToolInfoInternationalBankName(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_international_bank_name</code>.
     */
    public String getPayoutToolInfoInternationalBankName() {
        return (String) get(11);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_international_bank_address</code>.
     */
    public void setPayoutToolInfoInternationalBankAddress(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_international_bank_address</code>.
     */
    public String getPayoutToolInfoInternationalBankAddress() {
        return (String) get(12);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_international_bank_iban</code>.
     */
    public void setPayoutToolInfoInternationalBankIban(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_international_bank_iban</code>.
     */
    public String getPayoutToolInfoInternationalBankIban() {
        return (String) get(13);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_international_bank_bic</code>.
     */
    public void setPayoutToolInfoInternationalBankBic(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_international_bank_bic</code>.
     */
    public String getPayoutToolInfoInternationalBankBic() {
        return (String) get(14);
    }

    /**
     * Setter for <code>nw.payout_tool.payout_tool_info_international_bank_local_code</code>.
     */
    public void setPayoutToolInfoInternationalBankLocalCode(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>nw.payout_tool.payout_tool_info_international_bank_local_code</code>.
     */
    public String getPayoutToolInfoInternationalBankLocalCode() {
        return (String) get(15);
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
    // Record16 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, Long, String, LocalDateTime, String, PayoutToolInfo, String, String, String, String, String, String, String, String, String, String> fieldsRow() {
        return (Row16) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, Long, String, LocalDateTime, String, PayoutToolInfo, String, String, String, String, String, String, String, String, String, String> valuesRow() {
        return (Row16) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return PayoutTool.PAYOUT_TOOL.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return PayoutTool.PAYOUT_TOOL.CNTRCT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field4() {
        return PayoutTool.PAYOUT_TOOL.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return PayoutTool.PAYOUT_TOOL.CURRENCY_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<PayoutToolInfo> field6() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_RUSSIAN_BANK_ACCOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_RUSSIAN_BANK_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_RUSSIAN_BANK_POST_ACCOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_RUSSIAN_BANK_BIK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_INTERNATIONAL_BANK_ACCOUNT_HOLDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_INTERNATIONAL_BANK_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_INTERNATIONAL_BANK_ADDRESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_INTERNATIONAL_BANK_IBAN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field15() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_INTERNATIONAL_BANK_BIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field16() {
        return PayoutTool.PAYOUT_TOOL.PAYOUT_TOOL_INFO_INTERNATIONAL_BANK_LOCAL_CODE;
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
        return getCntrctId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getPayoutToolId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value4() {
        return getCreatedAt();
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
    public PayoutToolInfo value6() {
        return getPayoutToolInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getPayoutToolInfoRussianBankAccount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getPayoutToolInfoRussianBankName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getPayoutToolInfoRussianBankPostAccount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getPayoutToolInfoRussianBankBik();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getPayoutToolInfoInternationalBankAccountHolder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getPayoutToolInfoInternationalBankName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value13() {
        return getPayoutToolInfoInternationalBankAddress();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value14() {
        return getPayoutToolInfoInternationalBankIban();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value15() {
        return getPayoutToolInfoInternationalBankBic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value16() {
        return getPayoutToolInfoInternationalBankLocalCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value2(Long value) {
        setCntrctId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value3(String value) {
        setPayoutToolId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value4(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value5(String value) {
        setCurrencyCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value6(PayoutToolInfo value) {
        setPayoutToolInfo(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value7(String value) {
        setPayoutToolInfoRussianBankAccount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value8(String value) {
        setPayoutToolInfoRussianBankName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value9(String value) {
        setPayoutToolInfoRussianBankPostAccount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value10(String value) {
        setPayoutToolInfoRussianBankBik(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value11(String value) {
        setPayoutToolInfoInternationalBankAccountHolder(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value12(String value) {
        setPayoutToolInfoInternationalBankName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value13(String value) {
        setPayoutToolInfoInternationalBankAddress(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value14(String value) {
        setPayoutToolInfoInternationalBankIban(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value15(String value) {
        setPayoutToolInfoInternationalBankBic(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord value16(String value) {
        setPayoutToolInfoInternationalBankLocalCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayoutToolRecord values(Long value1, Long value2, String value3, LocalDateTime value4, String value5, PayoutToolInfo value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14, String value15, String value16) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PayoutToolRecord
     */
    public PayoutToolRecord() {
        super(PayoutTool.PAYOUT_TOOL);
    }

    /**
     * Create a detached, initialised PayoutToolRecord
     */
    public PayoutToolRecord(Long id, Long cntrctId, String payoutToolId, LocalDateTime createdAt, String currencyCode, PayoutToolInfo payoutToolInfo, String payoutToolInfoRussianBankAccount, String payoutToolInfoRussianBankName, String payoutToolInfoRussianBankPostAccount, String payoutToolInfoRussianBankBik, String payoutToolInfoInternationalBankAccountHolder, String payoutToolInfoInternationalBankName, String payoutToolInfoInternationalBankAddress, String payoutToolInfoInternationalBankIban, String payoutToolInfoInternationalBankBic, String payoutToolInfoInternationalBankLocalCode) {
        super(PayoutTool.PAYOUT_TOOL);

        set(0, id);
        set(1, cntrctId);
        set(2, payoutToolId);
        set(3, createdAt);
        set(4, currencyCode);
        set(5, payoutToolInfo);
        set(6, payoutToolInfoRussianBankAccount);
        set(7, payoutToolInfoRussianBankName);
        set(8, payoutToolInfoRussianBankPostAccount);
        set(9, payoutToolInfoRussianBankBik);
        set(10, payoutToolInfoInternationalBankAccountHolder);
        set(11, payoutToolInfoInternationalBankName);
        set(12, payoutToolInfoInternationalBankAddress);
        set(13, payoutToolInfoInternationalBankIban);
        set(14, payoutToolInfoInternationalBankBic);
        set(15, payoutToolInfoInternationalBankLocalCode);
    }
}