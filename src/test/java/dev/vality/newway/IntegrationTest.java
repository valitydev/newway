package dev.vality.newway;

import dev.vality.damsel.domain.*;
import dev.vality.damsel.domain.PaymentRoute;
import dev.vality.damsel.payment_processing.*;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.msgpack.Value;
import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.invoicing.iface.*;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.enums.PaymentStatus;
import dev.vality.newway.domain.tables.pojos.*;
import dev.vality.newway.service.InvoicingService;
import dev.vality.newway.utils.MockUtils;
import dev.vality.sink.common.serialization.impl.PaymentEventPayloadSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static dev.vality.newway.utils.JdbcUtil.countInvoiceEntity;
import static dev.vality.newway.utils.JdbcUtil.countPaymentEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlSpringBootITest
public class IntegrationTest {

    @Autowired
    private InvoicingService invoicingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PaymentStatusInfoDao paymentStatusInfoDao;
    @Autowired
    private PaymentPayerInfoDao paymentPayerInfoDao;
    @Autowired
    private PaymentAdditionalInfoDao paymentAdditionalInfoDao;
    @Autowired
    private PaymentRecurrentInfoDao paymentRecurrentInfoDao;
    @Autowired
    private PaymentRiskDataDao paymentRiskDataDao;
    @Autowired
    private PaymentFeeDao paymentFeeDao;
    @Autowired
    private PaymentRouteDao paymentRouteDao;
    @Autowired
    private CashFlowLinkDao cashFlowLinkDao;
    @Autowired
    private CashFlowDao cashFlowDao;

    private final PaymentEventPayloadSerializer serializer = new PaymentEventPayloadSerializer();

    // TODO: проверить сущности на входе и сущности в БД, рефактор.

    @Test
    public void test() {
        cleanUpTables();

        String invoiceId = "invoiceId";
        String paymentId = "paymentId";
        List<MachineEvent> machineEventsFirst = getInitialInvoicePaymentEvents(invoiceId, paymentId);
        invoicingService.handleEvents(machineEventsFirst);
        assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment " +
                        "WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());
        Payment payment = paymentDao.get(invoiceId, paymentId);
        PaymentStatusInfo statusInfo = paymentStatusInfoDao.get(invoiceId, paymentId);
        assertEquals(PaymentStatus.captured, statusInfo.getStatus());
        PaymentRiskData paymentRiskData = paymentRiskDataDao.get(invoiceId, paymentId);
        assertEquals("high", paymentRiskData.getRiskScore().name());
        CashFlowLink cashFlowLink = cashFlowLinkDao.get(invoiceId, paymentId);
        List<CashFlow> cashFlows = cashFlowDao.getByObjId(cashFlowLink.getId(), PaymentChangeType.payment);

        //--- second changes - only update

        List<MachineEvent> machineEventsSecond = getInvoicePaymentChanges(invoiceId, paymentId);
        invoicingService.handleEvents(machineEventsSecond);

        assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment " +
                        "WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());

        Payment paymentSecond = paymentDao.get(invoiceId, paymentId);
        PaymentRiskData paymentRiskDataSecond = paymentRiskDataDao.get(invoiceId, paymentId);
        assertEquals("low", paymentRiskDataSecond.getRiskScore().name());
        PaymentRecurrentInfo recurrentInfoSecond = paymentRecurrentInfoDao.get(invoiceId, paymentId);
        assertEquals("keks", recurrentInfoSecond.getToken());
        assertEquals(paymentSecond.getId(), payment.getId());

        //--- third changes - insert

        List<MachineEvent> machineEventsThird = getInvoicePaymentFailedChange(invoiceId, paymentId);
        invoicingService.handleEvents(machineEventsThird);

        assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment " +
                        "WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());

        PaymentStatusInfo statusInfoThird = paymentStatusInfoDao.get(invoiceId, paymentId);
        assertEquals(PaymentStatus.failed, statusInfoThird.getStatus());
        assertEquals(3, statusInfoThird.getSequenceId().longValue());

        //--- duplication check

        invoicingService.handleEvents(machineEventsFirst);
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment", invoiceId, paymentId, false));
        assertEquals(3, countPaymentEntity(jdbcTemplate, "payment_status_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_status_info", invoiceId, paymentId, true));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_payer_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_additional_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_additional_info", invoiceId, paymentId, true));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_recurrent_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_recurrent_info", invoiceId, paymentId, true));
        assertEquals(2, countPaymentEntity(jdbcTemplate, "payment_risk_data", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_risk_data", invoiceId, paymentId, true));
        assertEquals(2, countPaymentEntity(jdbcTemplate, "payment_fee", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_fee", invoiceId, paymentId, true));
        assertEquals(2, countPaymentEntity(jdbcTemplate, "payment_route", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_route", invoiceId, paymentId, true));
        assertEquals(2, countPaymentEntity(jdbcTemplate, "cash_flow_link", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "cash_flow_link", invoiceId, paymentId, true));

        assertEquals(2, countInvoiceEntity(jdbcTemplate, "invoice_status_info", invoiceId, false));
        assertEquals(1, countInvoiceEntity(jdbcTemplate, "invoice_status_info", invoiceId, true));
        assertEquals(1, countInvoiceEntity(jdbcTemplate, "invoice_cart", invoiceId, false));
    }

    @NotNull
    private List<MachineEvent> getInitialInvoicePaymentEvents(String invoiceId, String paymentId) {
        return List.of(
                new MachineEvent().setSourceId(invoiceId)
                        .setEventId(1)
                        .setCreatedAt(TypeUtil.temporalToString(LocalDateTime.now()))
                        .setData(Value.bin(serializer.serialize(
                                EventPayload.invoice_changes(
                                        List.of(InvoiceChange.invoice_created(new InvoiceCreated()
                                                        .setInvoice(MockUtils.buildInvoice(invoiceId))),
                                                InvoiceChange.invoice_status_changed(new InvoiceStatusChanged()
                                                        .setStatus(
                                                                InvoiceStatus.fulfilled(
                                                                        new InvoiceFulfilled("keks")))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_started(new InvoicePaymentStarted()
                                                                        .setPayment(
                                                                                MockUtils.buildPayment(paymentId))
                                                                        .setCashFlow(List.of(new FinalCashFlowPosting()
                                                                                .setSource(
                                                                                        new FinalCashFlowAccount(
                                                                                                CashFlowAccount
                                                                                                        .system(SystemCashFlowAccount.settlement),
                                                                                                1))
                                                                                .setDestination(
                                                                                        new FinalCashFlowAccount(
                                                                                                CashFlowAccount
                                                                                                        .system(SystemCashFlowAccount.settlement),
                                                                                                1))
                                                                                .setVolume(new Cash(2,
                                                                                        new CurrencyRef(
                                                                                                "RUB")))))
                                                                        .setRoute(new PaymentRoute(
                                                                                new ProviderRef(29),
                                                                                new TerminalRef(30)))
                                                                ))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_cash_flow_changed(
                                                                        new InvoicePaymentCashFlowChanged(
                                                                                List.of(new FinalCashFlowPosting()
                                                                                        .setSource(
                                                                                                new FinalCashFlowAccount(
                                                                                                        CashFlowAccount
                                                                                                                .system(SystemCashFlowAccount.settlement),
                                                                                                        1))
                                                                                        .setDestination(
                                                                                                new FinalCashFlowAccount(
                                                                                                        CashFlowAccount
                                                                                                                .system(SystemCashFlowAccount.settlement),
                                                                                                        1))
                                                                                        .setVolume(new Cash(1,
                                                                                                new CurrencyRef(
                                                                                                        "RUB")))))
                                                                ))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_risk_score_changed(
                                                                        new InvoicePaymentRiskScoreChanged(
                                                                                RiskScore.high)))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_status_changed(
                                                                        new InvoicePaymentStatusChanged().setStatus(
                                                                                InvoicePaymentStatus.captured(
                                                                                        new InvoicePaymentCaptured()))
                                                                ))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_session_change(
                                                                        new InvoicePaymentSessionChange()
                                                                                .setTarget(TargetInvoicePaymentStatus.processed(new InvoicePaymentProcessed()))
                                                                                .setPayload(SessionChangePayload.session_transaction_bound(
                                                                                        new SessionTransactionBound()
                                                                                                .setTrx(new TransactionInfo()
                                                                                                        .setId("trxId")
                                                                                                        .setExtra(Map.of("lol", "kek"))
                                                                                                ))))))
                                        ))))));
    }

    @NotNull
    private List<MachineEvent> getInvoicePaymentChanges(String invoiceId, String paymentId) {
        return List.of(
                new MachineEvent().setSourceId(invoiceId)
                        .setEventId(2)
                        .setCreatedAt(TypeUtil.temporalToString(LocalDateTime.now()))
                        .setData(Value.bin(serializer.serialize(
                                EventPayload.invoice_changes(
                                        List.of(InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(
                                                                InvoicePaymentChangePayload
                                                                        .invoice_payment_risk_score_changed(
                                                                                new InvoicePaymentRiskScoreChanged(
                                                                                        RiskScore.low)))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_rec_token_acquired(
                                                                        new InvoicePaymentRecTokenAcquired("keks")
                                                                ))),
                                                InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                        .setId(paymentId)
                                                        .setPayload(InvoicePaymentChangePayload
                                                                .invoice_payment_route_changed(
                                                                        new InvoicePaymentRouteChanged()
                                                                                .setRoute(new PaymentRoute(
                                                                                        new ProviderRef(29),
                                                                                        new TerminalRef(30)))
                                                                )))
                                        )))))
        );
    }

    @NotNull
    private List<MachineEvent> getInvoicePaymentFailedChange(String invoiceId, String paymentId) {
        return List.of(
                new MachineEvent().setSourceId(invoiceId)
                        .setEventId(3)
                        .setCreatedAt(TypeUtil.temporalToString(LocalDateTime.now()))
                        .setData(Value.bin(serializer.serialize(
                                EventPayload.invoice_changes(
                                        List.of(InvoiceChange.invoice_payment_change(new InvoicePaymentChange()
                                                .setId(paymentId)
                                                .setPayload(InvoicePaymentChangePayload.invoice_payment_status_changed(
                                                        new InvoicePaymentStatusChanged()
                                                                .setStatus(InvoicePaymentStatus.failed(
                                                                        new InvoicePaymentFailed(OperationFailure
                                                                                .operation_timeout(
                                                                                        new OperationTimeout()))))
                                                ))))))))
        );
    }

    private void cleanUpTables() {
        jdbcTemplate.execute("truncate table nw.invoice cascade");
        jdbcTemplate.execute("truncate table nw.invoice_status_info cascade");
        jdbcTemplate.execute("truncate table nw.invoice_cart cascade");
        jdbcTemplate.execute("truncate table nw.payment cascade");
        jdbcTemplate.execute("truncate table nw.payment_status_info cascade");
        jdbcTemplate.execute("truncate table nw.payment_payer_info cascade");
        jdbcTemplate.execute("truncate table nw.payment_additional_info cascade");
        jdbcTemplate.execute("truncate table nw.payment_recurrent_info cascade");
        jdbcTemplate.execute("truncate table nw.payment_risk_data cascade");
        jdbcTemplate.execute("truncate table nw.payment_fee cascade");
        jdbcTemplate.execute("truncate table nw.payment_route cascade");
        jdbcTemplate.execute("truncate table nw.cash_flow_link cascade");
        jdbcTemplate.execute("truncate table nw.cash_flow cascade");
    }

}
