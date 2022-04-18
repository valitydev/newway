package dev.vality.newway;

import dev.vality.damsel.domain.*;
import dev.vality.damsel.payment_processing.*;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.msgpack.Value;
import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.invoicing.iface.PaymentDao;
import dev.vality.newway.domain.enums.PaymentStatus;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.service.InvoicingService;
import dev.vality.newway.utils.MockUtils;
import dev.vality.sink.common.serialization.impl.PaymentEventPayloadSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@PostgresqlSpringBootITest
public class IntegrationTest {

    @Autowired
    private InvoicingService invoicingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PaymentDao paymentDao;

    @Test
    public void test() {
        PaymentEventPayloadSerializer serializer = new PaymentEventPayloadSerializer();
        String invoiceId = "inv_id";
        String paymentId = "1";
        List<MachineEvent> machineEventsFirst = List.of(
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
                                                                                MockUtils.buildPayment(paymentId))))),
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
                                                                ))))))))
        );
        invoicingService.handleEvents(machineEventsFirst);
        Assertions.assertEquals(3, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment " +
                        "WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());

        Payment payment = paymentDao.get(invoiceId, paymentId);
        Assertions.assertEquals(PaymentStatus.captured, payment.getStatus());
        Assertions.assertEquals("high", payment.getRiskScore().name());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.cash_flow WHERE obj_id = ? ",
                new Object[]{payment.getId()}, Integer.class)).intValue());

        //--- second changes - only update

        List<MachineEvent> machineEventsSecond = List.of(
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
                                                                )))
                                        )))))
        );

        invoicingService.handleEvents(machineEventsSecond);

        Assertions.assertEquals(3, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment " +
                        "WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());

        Payment paymentSecond = paymentDao.get(invoiceId, paymentId);
        Assertions.assertEquals("low", paymentSecond.getRiskScore().name());
        Assertions.assertEquals("keks", paymentSecond.getRecurrentIntentionToken());
        Assertions.assertEquals(paymentSecond.getId(), payment.getId());

        //--- third changes - insert

        List<MachineEvent> machineEventsThird = List.of(
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

        invoicingService.handleEvents(machineEventsThird);

        Assertions.assertEquals(4, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment " +
                        "WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());

        Payment paymentThird = paymentDao.get(invoiceId, paymentId);
        Assertions.assertEquals(PaymentStatus.failed, paymentThird.getStatus());
        Assertions.assertEquals(3, paymentThird.getSequenceId().longValue());
        Assertions.assertNotEquals(paymentSecond.getId(), paymentThird.getId());

        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.cash_flow WHERE obj_id = ? ",
                new Object[]{paymentThird.getId()}, Integer.class)).intValue());

        //--- duplication check

        invoicingService.handleEvents(machineEventsFirst);
        Assertions.assertEquals(4, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.payment WHERE invoice_id = ? and payment_id = ? ",
                new Object[]{invoiceId, paymentId}, Integer.class)).intValue());

        Assertions.assertEquals(2, Objects.requireNonNull(jdbcTemplate.queryForObject(
                "SELECT count(*) FROM nw.invoice_status_info  WHERE invoice_id = ?",
                new Object[]{invoiceId}, Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject(
                "SELECT count(*) FROM nw.invoice_status_info  WHERE invoice_id = ? and current",
                new Object[]{invoiceId}, Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate.queryForObject(
                "SELECT count(*) FROM nw.invoice_cart  WHERE invoice_id = ?",
                new Object[]{invoiceId}, Integer.class)).intValue());
    }
}
