/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain;


import com.rbkmoney.newway.domain.tables.Adjustment;
import com.rbkmoney.newway.domain.tables.Calendar;
import com.rbkmoney.newway.domain.tables.CashFlow;
import com.rbkmoney.newway.domain.tables.Category;
import com.rbkmoney.newway.domain.tables.Challenge;
import com.rbkmoney.newway.domain.tables.Contract;
import com.rbkmoney.newway.domain.tables.ContractAdjustment;
import com.rbkmoney.newway.domain.tables.Contractor;
import com.rbkmoney.newway.domain.tables.Currency;
import com.rbkmoney.newway.domain.tables.FistfulCashFlow;
import com.rbkmoney.newway.domain.tables.Inspector;
import com.rbkmoney.newway.domain.tables.Invoice;
import com.rbkmoney.newway.domain.tables.InvoiceCart;
import com.rbkmoney.newway.domain.tables.Party;
import com.rbkmoney.newway.domain.tables.Payment;
import com.rbkmoney.newway.domain.tables.PaymentInstitution;
import com.rbkmoney.newway.domain.tables.PaymentMethod;
import com.rbkmoney.newway.domain.tables.Payout;
import com.rbkmoney.newway.domain.tables.PayoutMethod;
import com.rbkmoney.newway.domain.tables.PayoutSummary;
import com.rbkmoney.newway.domain.tables.PayoutTool;
import com.rbkmoney.newway.domain.tables.Provider;
import com.rbkmoney.newway.domain.tables.Proxy;
import com.rbkmoney.newway.domain.tables.Refund;
import com.rbkmoney.newway.domain.tables.Shop;
import com.rbkmoney.newway.domain.tables.TermSetHierarchy;
import com.rbkmoney.newway.domain.tables.Terminal;
import com.rbkmoney.newway.domain.tables.Wallet;
import com.rbkmoney.newway.domain.tables.Withdrawal;
import com.rbkmoney.newway.domain.tables.records.AdjustmentRecord;
import com.rbkmoney.newway.domain.tables.records.CalendarRecord;
import com.rbkmoney.newway.domain.tables.records.CashFlowRecord;
import com.rbkmoney.newway.domain.tables.records.CategoryRecord;
import com.rbkmoney.newway.domain.tables.records.ChallengeRecord;
import com.rbkmoney.newway.domain.tables.records.ContractAdjustmentRecord;
import com.rbkmoney.newway.domain.tables.records.ContractRecord;
import com.rbkmoney.newway.domain.tables.records.ContractorRecord;
import com.rbkmoney.newway.domain.tables.records.CurrencyRecord;
import com.rbkmoney.newway.domain.tables.records.FistfulCashFlowRecord;
import com.rbkmoney.newway.domain.tables.records.IdentityRecord;
import com.rbkmoney.newway.domain.tables.records.InspectorRecord;
import com.rbkmoney.newway.domain.tables.records.InvoiceCartRecord;
import com.rbkmoney.newway.domain.tables.records.InvoiceRecord;
import com.rbkmoney.newway.domain.tables.records.PartyRecord;
import com.rbkmoney.newway.domain.tables.records.PaymentInstitutionRecord;
import com.rbkmoney.newway.domain.tables.records.PaymentMethodRecord;
import com.rbkmoney.newway.domain.tables.records.PaymentRecord;
import com.rbkmoney.newway.domain.tables.records.PayoutMethodRecord;
import com.rbkmoney.newway.domain.tables.records.PayoutRecord;
import com.rbkmoney.newway.domain.tables.records.PayoutSummaryRecord;
import com.rbkmoney.newway.domain.tables.records.PayoutToolRecord;
import com.rbkmoney.newway.domain.tables.records.ProviderRecord;
import com.rbkmoney.newway.domain.tables.records.ProxyRecord;
import com.rbkmoney.newway.domain.tables.records.RefundRecord;
import com.rbkmoney.newway.domain.tables.records.ShopRecord;
import com.rbkmoney.newway.domain.tables.records.TermSetHierarchyRecord;
import com.rbkmoney.newway.domain.tables.records.TerminalRecord;
import com.rbkmoney.newway.domain.tables.records.WalletRecord;
import com.rbkmoney.newway.domain.tables.records.WithdrawalRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>nw</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<AdjustmentRecord, Long> IDENTITY_ADJUSTMENT = Identities0.IDENTITY_ADJUSTMENT;
    public static final Identity<CalendarRecord, Long> IDENTITY_CALENDAR = Identities0.IDENTITY_CALENDAR;
    public static final Identity<CashFlowRecord, Long> IDENTITY_CASH_FLOW = Identities0.IDENTITY_CASH_FLOW;
    public static final Identity<CategoryRecord, Long> IDENTITY_CATEGORY = Identities0.IDENTITY_CATEGORY;
    public static final Identity<ChallengeRecord, Long> IDENTITY_CHALLENGE = Identities0.IDENTITY_CHALLENGE;
    public static final Identity<ContractRecord, Long> IDENTITY_CONTRACT = Identities0.IDENTITY_CONTRACT;
    public static final Identity<ContractAdjustmentRecord, Long> IDENTITY_CONTRACT_ADJUSTMENT = Identities0.IDENTITY_CONTRACT_ADJUSTMENT;
    public static final Identity<ContractorRecord, Long> IDENTITY_CONTRACTOR = Identities0.IDENTITY_CONTRACTOR;
    public static final Identity<CurrencyRecord, Long> IDENTITY_CURRENCY = Identities0.IDENTITY_CURRENCY;
    public static final Identity<FistfulCashFlowRecord, Long> IDENTITY_FISTFUL_CASH_FLOW = Identities0.IDENTITY_FISTFUL_CASH_FLOW;
    public static final Identity<IdentityRecord, Long> IDENTITY_IDENTITY = Identities0.IDENTITY_IDENTITY;
    public static final Identity<InspectorRecord, Long> IDENTITY_INSPECTOR = Identities0.IDENTITY_INSPECTOR;
    public static final Identity<InvoiceRecord, Long> IDENTITY_INVOICE = Identities0.IDENTITY_INVOICE;
    public static final Identity<InvoiceCartRecord, Long> IDENTITY_INVOICE_CART = Identities0.IDENTITY_INVOICE_CART;
    public static final Identity<PartyRecord, Long> IDENTITY_PARTY = Identities0.IDENTITY_PARTY;
    public static final Identity<PaymentRecord, Long> IDENTITY_PAYMENT = Identities0.IDENTITY_PAYMENT;
    public static final Identity<PaymentInstitutionRecord, Long> IDENTITY_PAYMENT_INSTITUTION = Identities0.IDENTITY_PAYMENT_INSTITUTION;
    public static final Identity<PaymentMethodRecord, Long> IDENTITY_PAYMENT_METHOD = Identities0.IDENTITY_PAYMENT_METHOD;
    public static final Identity<PayoutRecord, Long> IDENTITY_PAYOUT = Identities0.IDENTITY_PAYOUT;
    public static final Identity<PayoutMethodRecord, Long> IDENTITY_PAYOUT_METHOD = Identities0.IDENTITY_PAYOUT_METHOD;
    public static final Identity<PayoutSummaryRecord, Long> IDENTITY_PAYOUT_SUMMARY = Identities0.IDENTITY_PAYOUT_SUMMARY;
    public static final Identity<PayoutToolRecord, Long> IDENTITY_PAYOUT_TOOL = Identities0.IDENTITY_PAYOUT_TOOL;
    public static final Identity<ProviderRecord, Long> IDENTITY_PROVIDER = Identities0.IDENTITY_PROVIDER;
    public static final Identity<ProxyRecord, Long> IDENTITY_PROXY = Identities0.IDENTITY_PROXY;
    public static final Identity<RefundRecord, Long> IDENTITY_REFUND = Identities0.IDENTITY_REFUND;
    public static final Identity<ShopRecord, Long> IDENTITY_SHOP = Identities0.IDENTITY_SHOP;
    public static final Identity<TermSetHierarchyRecord, Long> IDENTITY_TERM_SET_HIERARCHY = Identities0.IDENTITY_TERM_SET_HIERARCHY;
    public static final Identity<TerminalRecord, Long> IDENTITY_TERMINAL = Identities0.IDENTITY_TERMINAL;
    public static final Identity<WalletRecord, Long> IDENTITY_WALLET = Identities0.IDENTITY_WALLET;
    public static final Identity<WithdrawalRecord, Long> IDENTITY_WITHDRAWAL = Identities0.IDENTITY_WITHDRAWAL;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AdjustmentRecord> ADJUSTMENT_PKEY = UniqueKeys0.ADJUSTMENT_PKEY;
    public static final UniqueKey<CalendarRecord> CALENDAR_PKEY = UniqueKeys0.CALENDAR_PKEY;
    public static final UniqueKey<CashFlowRecord> CASH_FLOW_PKEY = UniqueKeys0.CASH_FLOW_PKEY;
    public static final UniqueKey<CategoryRecord> CATEGORY_PKEY = UniqueKeys0.CATEGORY_PKEY;
    public static final UniqueKey<ChallengeRecord> CHALLENGE_PKEY = UniqueKeys0.CHALLENGE_PKEY;
    public static final UniqueKey<ContractRecord> CONTRACT_PKEY = UniqueKeys0.CONTRACT_PKEY;
    public static final UniqueKey<ContractAdjustmentRecord> CONTRACT_ADJUSTMENT_PKEY = UniqueKeys0.CONTRACT_ADJUSTMENT_PKEY;
    public static final UniqueKey<ContractorRecord> CONTRACTOR_PKEY = UniqueKeys0.CONTRACTOR_PKEY;
    public static final UniqueKey<CurrencyRecord> CURRENCY_PKEY = UniqueKeys0.CURRENCY_PKEY;
    public static final UniqueKey<FistfulCashFlowRecord> FISTFUL_CASH_FLOW_PKEY = UniqueKeys0.FISTFUL_CASH_FLOW_PKEY;
    public static final UniqueKey<IdentityRecord> IDENTITY_PKEY = UniqueKeys0.IDENTITY_PKEY;
    public static final UniqueKey<InspectorRecord> INSPECTOR_PKEY = UniqueKeys0.INSPECTOR_PKEY;
    public static final UniqueKey<InvoiceRecord> INVOICE_PKEY = UniqueKeys0.INVOICE_PKEY;
    public static final UniqueKey<InvoiceCartRecord> INVOICE_CART_PKEY = UniqueKeys0.INVOICE_CART_PKEY;
    public static final UniqueKey<PartyRecord> PARTY_PKEY = UniqueKeys0.PARTY_PKEY;
    public static final UniqueKey<PaymentRecord> PAYMENT_PKEY = UniqueKeys0.PAYMENT_PKEY;
    public static final UniqueKey<PaymentInstitutionRecord> PAYMENT_INSTITUTION_PKEY = UniqueKeys0.PAYMENT_INSTITUTION_PKEY;
    public static final UniqueKey<PaymentMethodRecord> PAYMENT_METHOD_PKEY = UniqueKeys0.PAYMENT_METHOD_PKEY;
    public static final UniqueKey<PayoutRecord> PAYOUT_PKEY = UniqueKeys0.PAYOUT_PKEY;
    public static final UniqueKey<PayoutMethodRecord> PAYOUT_METHOD_PKEY = UniqueKeys0.PAYOUT_METHOD_PKEY;
    public static final UniqueKey<PayoutSummaryRecord> PAYOUT_SUMMARY_PKEY = UniqueKeys0.PAYOUT_SUMMARY_PKEY;
    public static final UniqueKey<PayoutToolRecord> PAYOUT_TOOL_PKEY = UniqueKeys0.PAYOUT_TOOL_PKEY;
    public static final UniqueKey<ProviderRecord> PROVIDER_PKEY = UniqueKeys0.PROVIDER_PKEY;
    public static final UniqueKey<ProxyRecord> PROXY_PKEY = UniqueKeys0.PROXY_PKEY;
    public static final UniqueKey<RefundRecord> REFUND_PKEY = UniqueKeys0.REFUND_PKEY;
    public static final UniqueKey<ShopRecord> SHOP_PKEY = UniqueKeys0.SHOP_PKEY;
    public static final UniqueKey<TermSetHierarchyRecord> TERM_SET_HIERARCHY_PKEY = UniqueKeys0.TERM_SET_HIERARCHY_PKEY;
    public static final UniqueKey<TerminalRecord> TERMINAL_PKEY = UniqueKeys0.TERMINAL_PKEY;
    public static final UniqueKey<WalletRecord> WALLET_PKEY = UniqueKeys0.WALLET_PKEY;
    public static final UniqueKey<WithdrawalRecord> WITHDRAWAL_PKEY = UniqueKeys0.WITHDRAWAL_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ContractAdjustmentRecord, ContractRecord> CONTRACT_ADJUSTMENT__FK_ADJUSTMENT_TO_CONTRACT = ForeignKeys0.CONTRACT_ADJUSTMENT__FK_ADJUSTMENT_TO_CONTRACT;
    public static final ForeignKey<InvoiceCartRecord, InvoiceRecord> INVOICE_CART__FK_CART_TO_INVOICE = ForeignKeys0.INVOICE_CART__FK_CART_TO_INVOICE;
    public static final ForeignKey<PayoutSummaryRecord, PayoutRecord> PAYOUT_SUMMARY__FK_SUMMARY_TO_PAYOUT = ForeignKeys0.PAYOUT_SUMMARY__FK_SUMMARY_TO_PAYOUT;
    public static final ForeignKey<PayoutToolRecord, ContractRecord> PAYOUT_TOOL__FK_PAYOUT_TOOL_TO_CONTRACT = ForeignKeys0.PAYOUT_TOOL__FK_PAYOUT_TOOL_TO_CONTRACT;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<AdjustmentRecord, Long> IDENTITY_ADJUSTMENT = createIdentity(Adjustment.ADJUSTMENT, Adjustment.ADJUSTMENT.ID);
        public static Identity<CalendarRecord, Long> IDENTITY_CALENDAR = createIdentity(Calendar.CALENDAR, Calendar.CALENDAR.ID);
        public static Identity<CashFlowRecord, Long> IDENTITY_CASH_FLOW = createIdentity(CashFlow.CASH_FLOW, CashFlow.CASH_FLOW.ID);
        public static Identity<CategoryRecord, Long> IDENTITY_CATEGORY = createIdentity(Category.CATEGORY, Category.CATEGORY.ID);
        public static Identity<ChallengeRecord, Long> IDENTITY_CHALLENGE = createIdentity(Challenge.CHALLENGE, Challenge.CHALLENGE.ID);
        public static Identity<ContractRecord, Long> IDENTITY_CONTRACT = createIdentity(Contract.CONTRACT, Contract.CONTRACT.ID);
        public static Identity<ContractAdjustmentRecord, Long> IDENTITY_CONTRACT_ADJUSTMENT = createIdentity(ContractAdjustment.CONTRACT_ADJUSTMENT, ContractAdjustment.CONTRACT_ADJUSTMENT.ID);
        public static Identity<ContractorRecord, Long> IDENTITY_CONTRACTOR = createIdentity(Contractor.CONTRACTOR, Contractor.CONTRACTOR.ID);
        public static Identity<CurrencyRecord, Long> IDENTITY_CURRENCY = createIdentity(Currency.CURRENCY, Currency.CURRENCY.ID);
        public static Identity<FistfulCashFlowRecord, Long> IDENTITY_FISTFUL_CASH_FLOW = createIdentity(FistfulCashFlow.FISTFUL_CASH_FLOW, FistfulCashFlow.FISTFUL_CASH_FLOW.ID);
        public static Identity<IdentityRecord, Long> IDENTITY_IDENTITY = createIdentity(com.rbkmoney.newway.domain.tables.Identity.IDENTITY, com.rbkmoney.newway.domain.tables.Identity.IDENTITY.ID);
        public static Identity<InspectorRecord, Long> IDENTITY_INSPECTOR = createIdentity(Inspector.INSPECTOR, Inspector.INSPECTOR.ID);
        public static Identity<InvoiceRecord, Long> IDENTITY_INVOICE = createIdentity(Invoice.INVOICE, Invoice.INVOICE.ID);
        public static Identity<InvoiceCartRecord, Long> IDENTITY_INVOICE_CART = createIdentity(InvoiceCart.INVOICE_CART, InvoiceCart.INVOICE_CART.ID);
        public static Identity<PartyRecord, Long> IDENTITY_PARTY = createIdentity(Party.PARTY, Party.PARTY.ID);
        public static Identity<PaymentRecord, Long> IDENTITY_PAYMENT = createIdentity(Payment.PAYMENT, Payment.PAYMENT.ID);
        public static Identity<PaymentInstitutionRecord, Long> IDENTITY_PAYMENT_INSTITUTION = createIdentity(PaymentInstitution.PAYMENT_INSTITUTION, PaymentInstitution.PAYMENT_INSTITUTION.ID);
        public static Identity<PaymentMethodRecord, Long> IDENTITY_PAYMENT_METHOD = createIdentity(PaymentMethod.PAYMENT_METHOD, PaymentMethod.PAYMENT_METHOD.ID);
        public static Identity<PayoutRecord, Long> IDENTITY_PAYOUT = createIdentity(Payout.PAYOUT, Payout.PAYOUT.ID);
        public static Identity<PayoutMethodRecord, Long> IDENTITY_PAYOUT_METHOD = createIdentity(PayoutMethod.PAYOUT_METHOD, PayoutMethod.PAYOUT_METHOD.ID);
        public static Identity<PayoutSummaryRecord, Long> IDENTITY_PAYOUT_SUMMARY = createIdentity(PayoutSummary.PAYOUT_SUMMARY, PayoutSummary.PAYOUT_SUMMARY.ID);
        public static Identity<PayoutToolRecord, Long> IDENTITY_PAYOUT_TOOL = createIdentity(PayoutTool.PAYOUT_TOOL, PayoutTool.PAYOUT_TOOL.ID);
        public static Identity<ProviderRecord, Long> IDENTITY_PROVIDER = createIdentity(Provider.PROVIDER, Provider.PROVIDER.ID);
        public static Identity<ProxyRecord, Long> IDENTITY_PROXY = createIdentity(Proxy.PROXY, Proxy.PROXY.ID);
        public static Identity<RefundRecord, Long> IDENTITY_REFUND = createIdentity(Refund.REFUND, Refund.REFUND.ID);
        public static Identity<ShopRecord, Long> IDENTITY_SHOP = createIdentity(Shop.SHOP, Shop.SHOP.ID);
        public static Identity<TermSetHierarchyRecord, Long> IDENTITY_TERM_SET_HIERARCHY = createIdentity(TermSetHierarchy.TERM_SET_HIERARCHY, TermSetHierarchy.TERM_SET_HIERARCHY.ID);
        public static Identity<TerminalRecord, Long> IDENTITY_TERMINAL = createIdentity(Terminal.TERMINAL, Terminal.TERMINAL.ID);
        public static Identity<WalletRecord, Long> IDENTITY_WALLET = createIdentity(Wallet.WALLET, Wallet.WALLET.ID);
        public static Identity<WithdrawalRecord, Long> IDENTITY_WITHDRAWAL = createIdentity(Withdrawal.WITHDRAWAL, Withdrawal.WITHDRAWAL.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<AdjustmentRecord> ADJUSTMENT_PKEY = createUniqueKey(Adjustment.ADJUSTMENT, "adjustment_pkey", Adjustment.ADJUSTMENT.ID);
        public static final UniqueKey<CalendarRecord> CALENDAR_PKEY = createUniqueKey(Calendar.CALENDAR, "calendar_pkey", Calendar.CALENDAR.ID);
        public static final UniqueKey<CashFlowRecord> CASH_FLOW_PKEY = createUniqueKey(CashFlow.CASH_FLOW, "cash_flow_pkey", CashFlow.CASH_FLOW.ID);
        public static final UniqueKey<CategoryRecord> CATEGORY_PKEY = createUniqueKey(Category.CATEGORY, "category_pkey", Category.CATEGORY.ID);
        public static final UniqueKey<ChallengeRecord> CHALLENGE_PKEY = createUniqueKey(Challenge.CHALLENGE, "challenge_pkey", Challenge.CHALLENGE.ID);
        public static final UniqueKey<ContractRecord> CONTRACT_PKEY = createUniqueKey(Contract.CONTRACT, "contract_pkey", Contract.CONTRACT.ID);
        public static final UniqueKey<ContractAdjustmentRecord> CONTRACT_ADJUSTMENT_PKEY = createUniqueKey(ContractAdjustment.CONTRACT_ADJUSTMENT, "contract_adjustment_pkey", ContractAdjustment.CONTRACT_ADJUSTMENT.ID);
        public static final UniqueKey<ContractorRecord> CONTRACTOR_PKEY = createUniqueKey(Contractor.CONTRACTOR, "contractor_pkey", Contractor.CONTRACTOR.ID);
        public static final UniqueKey<CurrencyRecord> CURRENCY_PKEY = createUniqueKey(Currency.CURRENCY, "currency_pkey", Currency.CURRENCY.ID);
        public static final UniqueKey<FistfulCashFlowRecord> FISTFUL_CASH_FLOW_PKEY = createUniqueKey(FistfulCashFlow.FISTFUL_CASH_FLOW, "fistful_cash_flow_pkey", FistfulCashFlow.FISTFUL_CASH_FLOW.ID);
        public static final UniqueKey<IdentityRecord> IDENTITY_PKEY = createUniqueKey(com.rbkmoney.newway.domain.tables.Identity.IDENTITY, "identity_pkey", com.rbkmoney.newway.domain.tables.Identity.IDENTITY.ID);
        public static final UniqueKey<InspectorRecord> INSPECTOR_PKEY = createUniqueKey(Inspector.INSPECTOR, "inspector_pkey", Inspector.INSPECTOR.ID);
        public static final UniqueKey<InvoiceRecord> INVOICE_PKEY = createUniqueKey(Invoice.INVOICE, "invoice_pkey", Invoice.INVOICE.ID);
        public static final UniqueKey<InvoiceCartRecord> INVOICE_CART_PKEY = createUniqueKey(InvoiceCart.INVOICE_CART, "invoice_cart_pkey", InvoiceCart.INVOICE_CART.ID);
        public static final UniqueKey<PartyRecord> PARTY_PKEY = createUniqueKey(Party.PARTY, "party_pkey", Party.PARTY.ID);
        public static final UniqueKey<PaymentRecord> PAYMENT_PKEY = createUniqueKey(Payment.PAYMENT, "payment_pkey", Payment.PAYMENT.ID);
        public static final UniqueKey<PaymentInstitutionRecord> PAYMENT_INSTITUTION_PKEY = createUniqueKey(PaymentInstitution.PAYMENT_INSTITUTION, "payment_institution_pkey", PaymentInstitution.PAYMENT_INSTITUTION.ID);
        public static final UniqueKey<PaymentMethodRecord> PAYMENT_METHOD_PKEY = createUniqueKey(PaymentMethod.PAYMENT_METHOD, "payment_method_pkey", PaymentMethod.PAYMENT_METHOD.ID);
        public static final UniqueKey<PayoutRecord> PAYOUT_PKEY = createUniqueKey(Payout.PAYOUT, "payout_pkey", Payout.PAYOUT.ID);
        public static final UniqueKey<PayoutMethodRecord> PAYOUT_METHOD_PKEY = createUniqueKey(PayoutMethod.PAYOUT_METHOD, "payout_method_pkey", PayoutMethod.PAYOUT_METHOD.ID);
        public static final UniqueKey<PayoutSummaryRecord> PAYOUT_SUMMARY_PKEY = createUniqueKey(PayoutSummary.PAYOUT_SUMMARY, "payout_summary_pkey", PayoutSummary.PAYOUT_SUMMARY.ID);
        public static final UniqueKey<PayoutToolRecord> PAYOUT_TOOL_PKEY = createUniqueKey(PayoutTool.PAYOUT_TOOL, "payout_tool_pkey", PayoutTool.PAYOUT_TOOL.ID);
        public static final UniqueKey<ProviderRecord> PROVIDER_PKEY = createUniqueKey(Provider.PROVIDER, "provider_pkey", Provider.PROVIDER.ID);
        public static final UniqueKey<ProxyRecord> PROXY_PKEY = createUniqueKey(Proxy.PROXY, "proxy_pkey", Proxy.PROXY.ID);
        public static final UniqueKey<RefundRecord> REFUND_PKEY = createUniqueKey(Refund.REFUND, "refund_pkey", Refund.REFUND.ID);
        public static final UniqueKey<ShopRecord> SHOP_PKEY = createUniqueKey(Shop.SHOP, "shop_pkey", Shop.SHOP.ID);
        public static final UniqueKey<TermSetHierarchyRecord> TERM_SET_HIERARCHY_PKEY = createUniqueKey(TermSetHierarchy.TERM_SET_HIERARCHY, "term_set_hierarchy_pkey", TermSetHierarchy.TERM_SET_HIERARCHY.ID);
        public static final UniqueKey<TerminalRecord> TERMINAL_PKEY = createUniqueKey(Terminal.TERMINAL, "terminal_pkey", Terminal.TERMINAL.ID);
        public static final UniqueKey<WalletRecord> WALLET_PKEY = createUniqueKey(Wallet.WALLET, "wallet_pkey", Wallet.WALLET.ID);
        public static final UniqueKey<WithdrawalRecord> WITHDRAWAL_PKEY = createUniqueKey(Withdrawal.WITHDRAWAL, "withdrawal_pkey", Withdrawal.WITHDRAWAL.ID);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<ContractAdjustmentRecord, ContractRecord> CONTRACT_ADJUSTMENT__FK_ADJUSTMENT_TO_CONTRACT = createForeignKey(com.rbkmoney.newway.domain.Keys.CONTRACT_PKEY, ContractAdjustment.CONTRACT_ADJUSTMENT, "contract_adjustment__fk_adjustment_to_contract", ContractAdjustment.CONTRACT_ADJUSTMENT.CNTRCT_ID);
        public static final ForeignKey<InvoiceCartRecord, InvoiceRecord> INVOICE_CART__FK_CART_TO_INVOICE = createForeignKey(com.rbkmoney.newway.domain.Keys.INVOICE_PKEY, InvoiceCart.INVOICE_CART, "invoice_cart__fk_cart_to_invoice", InvoiceCart.INVOICE_CART.INV_ID);
        public static final ForeignKey<PayoutSummaryRecord, PayoutRecord> PAYOUT_SUMMARY__FK_SUMMARY_TO_PAYOUT = createForeignKey(com.rbkmoney.newway.domain.Keys.PAYOUT_PKEY, PayoutSummary.PAYOUT_SUMMARY, "payout_summary__fk_summary_to_payout", PayoutSummary.PAYOUT_SUMMARY.PYT_ID);
        public static final ForeignKey<PayoutToolRecord, ContractRecord> PAYOUT_TOOL__FK_PAYOUT_TOOL_TO_CONTRACT = createForeignKey(com.rbkmoney.newway.domain.Keys.CONTRACT_PKEY, PayoutTool.PAYOUT_TOOL, "payout_tool__fk_payout_tool_to_contract", PayoutTool.PAYOUT_TOOL.CNTRCT_ID);
    }
}
