package com.rbkmoney.newway;

import com.rbkmoney.damsel.domain.InvoicePaymentChargeback;
import com.rbkmoney.damsel.domain.*;
import com.rbkmoney.damsel.payment_processing.*;
import com.rbkmoney.geck.common.util.TypeUtil;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static InvoiceChange buildInvoiceChangeChargebackCreated() {
        InvoicePaymentChargeback invoicePaymentChargeback =
                EnhancedRandom.random(InvoicePaymentChargeback.class, "context", "status", "reason", "stage");
        invoicePaymentChargeback.setCreatedAt(TypeUtil.temporalToString(Instant.now()));
        InvoicePaymentChargebackStatus invoicePaymentChargebackStatus = buildChargebackStatus();
        invoicePaymentChargeback.setStatus(invoicePaymentChargebackStatus);

        InvoicePaymentChargebackReason invoicePaymentChargebackReason = buildChargebackReason();
        invoicePaymentChargeback.setReason(invoicePaymentChargebackReason);

        InvoicePaymentChargebackStage invoicePaymentChargebackStage = buildChargebackStage();
        invoicePaymentChargeback.setStage(invoicePaymentChargebackStage);

        InvoicePaymentChargebackChange invoicePaymentChargebackChange = new InvoicePaymentChargebackChange();
        invoicePaymentChargebackChange.setId("testChargebackId");
        InvoicePaymentChargebackChangePayload invoicePaymentChargebackChangePayload =
                new InvoicePaymentChargebackChangePayload();
        InvoicePaymentChargebackCreated invoicePaymentChargebackCreated = new InvoicePaymentChargebackCreated();
        invoicePaymentChargebackCreated.setChargeback(invoicePaymentChargeback);

        invoicePaymentChargebackChangePayload.setInvoicePaymentChargebackCreated(invoicePaymentChargebackCreated);
        invoicePaymentChargebackChange.setPayload(invoicePaymentChargebackChangePayload);

        InvoicePaymentChangePayload invoicePaymentChangePayload = new InvoicePaymentChangePayload();
        invoicePaymentChangePayload.setInvoicePaymentChargebackChange(invoicePaymentChargebackChange);

        InvoicePaymentChange invoicePaymentChange = new InvoicePaymentChange();
        invoicePaymentChange.setId("testPaymentId");
        invoicePaymentChange.setPayload(invoicePaymentChangePayload);

        InvoiceChange invoiceChange = new InvoiceChange();
        invoiceChange.setInvoicePaymentChange(invoicePaymentChange);

        return invoiceChange;
    }

    public static InvoiceChange buildInvoiceChangeChargebackStatusChanged() {
        InvoicePaymentChargebackStatusChanged invoicePaymentChargebackStatusChanged =
                new InvoicePaymentChargebackStatusChanged();
        invoicePaymentChargebackStatusChanged.setStatus(buildChargebackStatus());
        InvoicePaymentChargebackChangePayload invoicePaymentChargebackChangePayload =
                new InvoicePaymentChargebackChangePayload();
        invoicePaymentChargebackChangePayload
                .setInvoicePaymentChargebackStatusChanged(invoicePaymentChargebackStatusChanged);

        return buildInvoiceChangeChargeback(invoicePaymentChargebackChangePayload);
    }

    public static InvoiceChange buildInvoiceChangeChargebackLevyChanged() {
        InvoicePaymentChargebackLevyChanged invoicePaymentChargebackLevyChanged =
                new InvoicePaymentChargebackLevyChanged();
        Cash cash = new Cash().setAmount(1000L).setCurrency(new CurrencyRef().setSymbolicCode("456"));
        invoicePaymentChargebackLevyChanged.setLevy(cash);
        InvoicePaymentChargebackChangePayload invoicePaymentChargebackChangePayload =
                new InvoicePaymentChargebackChangePayload();
        invoicePaymentChargebackChangePayload
                .setInvoicePaymentChargebackLevyChanged(invoicePaymentChargebackLevyChanged);

        return buildInvoiceChangeChargeback(invoicePaymentChargebackChangePayload);
    }

    public static InvoiceChange buildInvoiceChangeChargebackStageChanged() {
        InvoicePaymentChargebackStageChanged invoicePaymentChargebackStageChanged =
                new InvoicePaymentChargebackStageChanged();
        invoicePaymentChargebackStageChanged.setStage(buildChargebackStage());
        InvoicePaymentChargebackChangePayload invoicePaymentChargebackChangePayload =
                new InvoicePaymentChargebackChangePayload();
        invoicePaymentChargebackChangePayload
                .setInvoicePaymentChargebackStageChanged(invoicePaymentChargebackStageChanged);

        return buildInvoiceChangeChargeback(invoicePaymentChargebackChangePayload);
    }

    public static InvoiceChange buildInvoiceChangeChargebackCashFlowChanged() {
        InvoicePaymentChargebackCashFlowChanged invoicePaymentChargebackCashFlowChanged =
                new InvoicePaymentChargebackCashFlowChanged();
        invoicePaymentChargebackCashFlowChanged.setCashFlow(Collections.singletonList(buildCashFlowPosting()));
        InvoicePaymentChargebackChangePayload invoicePaymentChargebackChangePayload =
                new InvoicePaymentChargebackChangePayload();
        invoicePaymentChargebackChangePayload
                .setInvoicePaymentChargebackCashFlowChanged(invoicePaymentChargebackCashFlowChanged);

        return buildInvoiceChangeChargeback(invoicePaymentChargebackChangePayload);
    }

    public static InvoiceChange buildInvoiceChangeChargebackBodyChanged() {
        InvoicePaymentChargebackBodyChanged invoicePaymentChargebackBodyChanged =
                new InvoicePaymentChargebackBodyChanged();
        Cash cash = new Cash().setAmount(1000).setCurrency(new CurrencyRef("653"));
        invoicePaymentChargebackBodyChanged.setBody(cash);
        InvoicePaymentChargebackChangePayload invoicePaymentChargebackChangePayload =
                new InvoicePaymentChargebackChangePayload();
        invoicePaymentChargebackChangePayload
                .setInvoicePaymentChargebackBodyChanged(invoicePaymentChargebackBodyChanged);

        return buildInvoiceChangeChargeback(invoicePaymentChargebackChangePayload);
    }

    private static InvoiceChange buildInvoiceChangeChargeback(InvoicePaymentChargebackChangePayload payload) {
        InvoicePaymentChargeback invoicePaymentChargeback =
                EnhancedRandom.random(InvoicePaymentChargeback.class, "context", "status", "reason", "stage");
        invoicePaymentChargeback.setCreatedAt(TypeUtil.temporalToString(Instant.now()));
        InvoicePaymentChargebackStatus invoicePaymentChargebackStatus = buildChargebackStatus();
        invoicePaymentChargeback.setStatus(invoicePaymentChargebackStatus);

        InvoicePaymentChargebackReason invoicePaymentChargebackReason = buildChargebackReason();
        invoicePaymentChargeback.setReason(invoicePaymentChargebackReason);

        InvoicePaymentChargebackStage invoicePaymentChargebackStage = buildChargebackStage();
        invoicePaymentChargeback.setStage(invoicePaymentChargebackStage);

        InvoicePaymentChargebackChange invoicePaymentChargebackChange = new InvoicePaymentChargebackChange();
        invoicePaymentChargebackChange.setId("testChargebackId");
        invoicePaymentChargebackChange.setPayload(payload);

        InvoicePaymentChangePayload invoicePaymentChangePayload = new InvoicePaymentChangePayload();
        invoicePaymentChangePayload.setInvoicePaymentChargebackChange(invoicePaymentChargebackChange);

        InvoicePaymentChange invoicePaymentChange = new InvoicePaymentChange();
        invoicePaymentChange.setId("testPaymentId");
        invoicePaymentChange.setPayload(invoicePaymentChangePayload);

        InvoiceChange invoiceChange = new InvoiceChange();
        invoiceChange.setInvoicePaymentChange(invoicePaymentChange);

        return invoiceChange;
    }

    private static FinalCashFlowPosting buildCashFlowPosting() {
        FinalCashFlowAccount sourceFinalCashFlowAccount = new FinalCashFlowAccount();
        sourceFinalCashFlowAccount.setAccountId(12345);
        sourceFinalCashFlowAccount.setAccountType(CashFlowAccount.merchant(MerchantCashFlowAccount.payout));
        FinalCashFlowAccount destFinalCashFlowAccount = new FinalCashFlowAccount();
        destFinalCashFlowAccount.setAccountId(56789);
        destFinalCashFlowAccount.setAccountType(CashFlowAccount.provider(ProviderCashFlowAccount.settlement));
        FinalCashFlowPosting cashFlowPosting = new FinalCashFlowPosting();
        cashFlowPosting.setDetails("testDetails");
        cashFlowPosting.setSource(sourceFinalCashFlowAccount);
        cashFlowPosting.setDestination(destFinalCashFlowAccount);
        Cash cash = new Cash();
        cash.setAmount(1000);
        cash.setCurrency(new CurrencyRef("643"));
        cashFlowPosting.setVolume(cash);

        return cashFlowPosting;
    }

    private static InvoicePaymentChargebackReason buildChargebackReason() {
        InvoicePaymentChargebackReason invoicePaymentChargebackReason = new InvoicePaymentChargebackReason();
        invoicePaymentChargebackReason.setCode("testCode");
        InvoicePaymentChargebackCategory invoicePaymentChargebackCategory = new InvoicePaymentChargebackCategory();
        invoicePaymentChargebackCategory.setFraud(new InvoicePaymentChargebackCategoryFraud());
        invoicePaymentChargebackReason.setCategory(invoicePaymentChargebackCategory);

        return invoicePaymentChargebackReason;
    }

    private static InvoicePaymentChargebackStatus buildChargebackStatus() {
        InvoicePaymentChargebackStatus invoicePaymentChargebackStatus = new InvoicePaymentChargebackStatus();
        invoicePaymentChargebackStatus.setAccepted(new InvoicePaymentChargebackAccepted());

        return invoicePaymentChargebackStatus;
    }

    private static InvoicePaymentChargebackStage buildChargebackStage() {
        InvoicePaymentChargebackStage invoicePaymentChargebackStage = new InvoicePaymentChargebackStage();
        invoicePaymentChargebackStage.setChargeback(new InvoicePaymentChargebackStageChargeback());

        return invoicePaymentChargebackStage;
    }

    public static Contractor buildContractor() {
        Contractor contractor = new Contractor();
        LegalEntity legalEntity = new LegalEntity();
        contractor.setLegalEntity(legalEntity);
        InternationalLegalEntity internationalLegalEntity = new InternationalLegalEntity();
        legalEntity.setInternationalLegalEntity(internationalLegalEntity);
        internationalLegalEntity
                .setCountry(new CountryRef().setId(CountryCode.findByValue(CountryCode.AUT.getValue())));
        internationalLegalEntity.setLegalName(randomString());
        internationalLegalEntity.setActualAddress(randomString());
        internationalLegalEntity.setRegisteredAddress(randomString());
        return contractor;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static CountryObject buildCountryObject() {
        Country country = new Country();
        country.setName(randomString());
        country.setTradeBlocs(Set.of(new TradeBlocRef().setId(randomString())));
        CountryObject countryObject = new CountryObject();
        countryObject.setData(country);
        countryObject.setRef(new CountryRef().setId(CountryCode.ABH));
        return countryObject;
    }

    public static TradeBlocObject buildTradeBlocObject() {
        TradeBloc tradeBloc = new TradeBloc();
        tradeBloc.setName(randomString());
        tradeBloc.setDescription(randomString());
        TradeBlocObject tradeBlocObject = new TradeBlocObject();
        tradeBlocObject.setData(tradeBloc);
        tradeBlocObject.setRef(new TradeBlocRef().setId(randomString()));
        return tradeBlocObject;
    }

    public static InvoicePaymentAdjustment createTestInvoicePaymentAdjustment() {
        return createTestInvoicePaymentAdjustment(new ArrayList<>(), new ArrayList<>());
    }

    public static InvoicePaymentAdjustment createTestInvoicePaymentAdjustment(List<FinalCashFlowPosting> oldCashFlow,
                                                                              List<FinalCashFlowPosting> newCashFlow) {
        InvoicePaymentAdjustment adjustment = new InvoicePaymentAdjustment();
        adjustment.setId("Adj1");
        adjustment.setState(InvoicePaymentAdjustmentState.cash_flow(
                new InvoicePaymentAdjustmentCashFlowState()
                        .setScenario(new InvoicePaymentAdjustmentCashFlow().setDomainRevision(1))
        ));
        adjustment.setCreatedAt(Instant.now().toString());
        adjustment.setDomainRevision(1L);
        adjustment.setReason("Test");
        adjustment.setNewCashFlow(newCashFlow);
        adjustment.setOldCashFlowInverse(oldCashFlow);
        adjustment.setPartyRevision(1L);
        adjustment.setStatus(InvoicePaymentAdjustmentStatus.captured(new InvoicePaymentAdjustmentCaptured()));
        return adjustment;
    }

    private static List<FinalCashFlowPosting> createTestFinalCashFlowPostings() {
        List<FinalCashFlowPosting> postings = new ArrayList<>();
        postings.add(
                new FinalCashFlowPosting(
                        new FinalCashFlowAccount(CashFlowAccount.provider(ProviderCashFlowAccount.settlement), 1),
                        new FinalCashFlowAccount(CashFlowAccount.merchant(MerchantCashFlowAccount.guarantee), 2),
                        new Cash(5, new CurrencyRef("RUB")))
        );
        return postings;
    }

    private static FinalCashFlowPosting createTestFinalCashFlowPosting() {
        return new FinalCashFlowPosting(
                new FinalCashFlowAccount(CashFlowAccount.provider(ProviderCashFlowAccount.settlement), 1),
                new FinalCashFlowAccount(CashFlowAccount.merchant(MerchantCashFlowAccount.guarantee), 2),
                new Cash(5, new CurrencyRef("RUB")));
    }

    public static FinalCashFlowAccount createMerchantAccount() {
        return new FinalCashFlowAccount(CashFlowAccount.merchant(MerchantCashFlowAccount.settlement), 2);
    }

    public static FinalCashFlowAccount createProviderAccount() {
        return new FinalCashFlowAccount(CashFlowAccount.provider(ProviderCashFlowAccount.settlement), 2);
    }

    public static FinalCashFlowAccount createSystemAccount() {
        return new FinalCashFlowAccount(CashFlowAccount.system(SystemCashFlowAccount.settlement), 1);
    }

    public static FinalCashFlowAccount createSubagentAccount() {
        return new FinalCashFlowAccount(CashFlowAccount.system(SystemCashFlowAccount.subagent), 1);
    }

    public static FinalCashFlowAccount createExternalIncomeAccount() {
        return new FinalCashFlowAccount(CashFlowAccount.external(ExternalCashFlowAccount.income), 1);
    }

    public static FinalCashFlowAccount createExternalOutcomeAccount() {
        return new FinalCashFlowAccount(CashFlowAccount.external(ExternalCashFlowAccount.outcome), 1);
    }

    public static Cash createTestCash(long amount) {
        return new Cash(amount, new CurrencyRef("RUB"));
    }

}
