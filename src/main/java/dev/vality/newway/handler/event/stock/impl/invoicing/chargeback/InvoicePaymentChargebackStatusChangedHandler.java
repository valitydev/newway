package dev.vality.newway.handler.event.stock.impl.invoicing.chargeback;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChargebackChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChargebackStatusChanged;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.invoicing.iface.ChargebackDao;
import dev.vality.newway.domain.enums.ChargebackStatus;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.Chargeback;
import dev.vality.newway.factory.machine.event.MachineEventCopyFactory;
import dev.vality.newway.handler.event.stock.impl.invoicing.InvoicingHandler;
import dev.vality.newway.service.CashFlowService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentChargebackStatusChangedHandler implements InvoicingHandler {


    private final ChargebackDao chargebackDao;
    private final CashFlowService cashFlowService;
    private final MachineEventCopyFactory<Chargeback, Integer> machineEventCopyFactory;

    @Getter
    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change" +
                    ".payload.invoice_payment_chargeback_change.payload.invoice_payment_chargeback_status_changed",
            new IsNullCondition().not()));

    @Override
    public void handle(InvoiceChange change, MachineEvent event, Integer changeId) {
        long sequenceId = event.getEventId();
        String invoiceId = event.getSourceId();
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String paymentId = invoicePaymentChange.getId();
        InvoicePaymentChargebackChange invoicePaymentChargebackChange =
                invoicePaymentChange.getPayload().getInvoicePaymentChargebackChange();
        InvoicePaymentChargebackStatusChanged invoicePaymentChargebackStatusChanged =
                invoicePaymentChargebackChange.getPayload().getInvoicePaymentChargebackStatusChanged();
        String chargebackId = invoicePaymentChargebackChange.getId();

        log.info(
                "Start chargeback status changed handling, " +
                        "sequenceId={}, invoiceId={}, paymentId={}, chargebackId={}, status={}",
                sequenceId, invoiceId, paymentId, chargebackId,
                invoicePaymentChargebackStatusChanged.getStatus().getSetField().getFieldName());

        Chargeback chargebackOld = chargebackDao.get(invoiceId, paymentId, chargebackId);
        Chargeback chargebackNew = machineEventCopyFactory.create(event, sequenceId, changeId, chargebackOld, null);

        chargebackNew.setStatus(
                TBaseUtil.unionFieldToEnum(invoicePaymentChargebackStatusChanged.getStatus(), ChargebackStatus.class));

        chargebackDao.save(chargebackNew).ifPresentOrElse(
                id -> {
                    Long oldId = chargebackOld.getId();
                    chargebackDao.updateNotCurrent(oldId);
                    cashFlowService.save(oldId, id, PaymentChangeType.chargeback);
                    log.info("Chargeback status changed have been succeeded, " +
                                    "sequenceId={}, invoiceId={}, paymentId={}, chargebackId={}",
                            sequenceId, invoiceId, paymentId, chargebackId);
                },
                () -> log.info("Chargeback status changed bound duplicated, " +
                                "sequenceId={}, invoiceId={}, paymentId={}, chargebackId={}",
                        sequenceId, invoiceId, paymentId, chargebackId));
    }

}
