package dev.vality.newway;

import dev.vality.damsel.domain.InvoicePaymentChargeback;
import dev.vality.damsel.domain.*;
import dev.vality.damsel.payment_processing.*;
import dev.vality.fistful.withdrawal.AdjustmentChange;
import dev.vality.fistful.withdrawal.Change;
import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.fistful.withdrawal.adjustment.*;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.msgpack.Value;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentStatus;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentType;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static InvoiceChange buildInvoiceChangeChargebackCreated() {
        InvoicePaymentChargeback invoicePaymentChargeback =
                dev.vality.testcontainers.annotations.util.RandomBeans.random(InvoicePaymentChargeback.class, "context", "status", "reason", "stage");
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
                dev.vality.testcontainers.annotations.util.RandomBeans.random(InvoicePaymentChargeback.class, "context", "status", "reason", "stage");
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

    public static TimestampedChange createWithdrawalAdjustmentCreatedStatusChange(String id) {
        Adjustment adjustment = new Adjustment();
        adjustment.setId(id);
        adjustment.setOperationTimestamp("2023-07-03T10:15:30Z");
        adjustment.setCreatedAt("2023-07-03T10:15:30Z");
        adjustment.setStatus(Status.pending(new Pending()));
        var newStatus = new dev.vality.fistful.withdrawal.status.Status();
        newStatus.setPending(new dev.vality.fistful.withdrawal.status.Pending());
        adjustment.setChangesPlan(
                new ChangesPlan()
                        .setNewStatus(new StatusChangePlan().setNewStatus(newStatus))
        );
        var payload = new dev.vality.fistful.withdrawal.adjustment.Change();
        payload.setCreated(new CreatedChange().setAdjustment(adjustment));
        AdjustmentChange adjustmentChange = new AdjustmentChange();
        adjustmentChange.setId("id");
        adjustmentChange.setPayload(payload);
        Change change = new Change();
        change.setAdjustment(adjustmentChange);
        TimestampedChange timestampedChange = new TimestampedChange();
        timestampedChange.setOccuredAt("2023-07-03T10:15:30Z");
        timestampedChange.setChange(change);
        return timestampedChange;
    }

    public static TimestampedChange createWithdrawalAdjustmentCreatedDomainRevisionChange(String id) {
        Adjustment adjustment = new Adjustment();
        adjustment.setId(id);
        adjustment.setOperationTimestamp("2023-07-03T10:15:30Z");
        adjustment.setCreatedAt("2023-07-03T10:15:30Z");
        adjustment.setStatus(Status.pending(new Pending()));
        var newStatus = new dev.vality.fistful.withdrawal.status.Status();
        newStatus.setPending(new dev.vality.fistful.withdrawal.status.Pending());
        adjustment.setChangesPlan(
                new ChangesPlan()
                        .setNewDomainRevision(new DataRevisionChangePlan().setNewDomainRevision(1L))
        );
        var payload = new dev.vality.fistful.withdrawal.adjustment.Change();
        payload.setCreated(new CreatedChange().setAdjustment(adjustment));
        AdjustmentChange adjustmentChange = new AdjustmentChange();
        adjustmentChange.setId("id");
        adjustmentChange.setPayload(payload);
        Change change = new Change();
        change.setAdjustment(adjustmentChange);
        TimestampedChange timestampedChange = new TimestampedChange();
        timestampedChange.setOccuredAt("2023-07-03T10:15:30Z");
        timestampedChange.setChange(change);
        return timestampedChange;
    }

    public static TimestampedChange createWithdrawalAdjustmentStatusChange(String id) {
        var payload = new dev.vality.fistful.withdrawal.adjustment.Change();
        payload.setStatusChanged(new StatusChange(Status.succeeded(new Succeeded())));
        AdjustmentChange adjustmentChange = new AdjustmentChange();
        adjustmentChange.setId(id);
        adjustmentChange.setPayload(payload);
        Change change = new Change();
        change.setAdjustment(adjustmentChange);
        TimestampedChange timestampedChange = new TimestampedChange();
        timestampedChange.setOccuredAt("2023-07-03T10:15:30Z");
        timestampedChange.setChange(change);
        return timestampedChange;
    }

    public static MachineEvent createWithdrawalAdjustmentdMachineEvent(TimestampedChange timestampedChange) {
        return new MachineEvent()
                .setEventId(2L)
                .setSourceId("sourceId")
                .setSourceNs("2")
                .setCreatedAt("2021-05-31T06:12:27Z")
                .setData(Value.bin(new ThriftSerializer<>().serialize("", timestampedChange)));
    }

    public static WithdrawalAdjustment createWithdrawalAdjustment(String id) {
        WithdrawalAdjustment withdrawalAdjustment = new WithdrawalAdjustment();
        withdrawalAdjustment.setType(WithdrawalAdjustmentType.domain_revision);
        withdrawalAdjustment.setStatus(WithdrawalAdjustmentStatus.pending);
        withdrawalAdjustment.setSequenceId(1L);
        withdrawalAdjustment.setAdjustmentId(id);
        withdrawalAdjustment.setDomainRevision(1L);
        withdrawalAdjustment.setCurrent(true);
        withdrawalAdjustment.setPartyRevision(2L);
        withdrawalAdjustment.setWithdrawalId("withdrawalId");
        withdrawalAdjustment.setExternalId("id");
        withdrawalAdjustment.setEventOccuredAt(LocalDateTime.now());
        withdrawalAdjustment.setEventCreatedAt(LocalDateTime.now());
        withdrawalAdjustment.setWtime(LocalDateTime.now());
        return withdrawalAdjustment;
    }

}
