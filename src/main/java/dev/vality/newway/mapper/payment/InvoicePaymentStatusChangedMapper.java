package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.InvoicePaymentStatus;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.PaymentStatusInfo;
import dev.vality.newway.mapper.Mapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.util.PaymentStatusInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentStatusChangedMapper implements Mapper<PaymentWrapper> {

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_status_changed",
            new IsNullCondition().not()
    ));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId) {
        InvoicePaymentStatus invoicePaymentStatus =
                change.getInvoicePaymentChange().getPayload().getInvoicePaymentStatusChanged().getStatus();
        long sequenceId = event.getEventId();
        String invoiceId = event.getSourceId();
        String paymentId = change.getInvoicePaymentChange().getId();

        log.info("Start payment status changed mapping, sequenceId={}, changeId={}, invoiceId={}, paymentId={}, status={}",
                sequenceId, changeId, invoiceId, paymentId, invoicePaymentStatus.getSetField().getFieldName());

        PaymentStatusInfo statusInfo = PaymentStatusInfoUtil.getPaymentStatusInfo(
                invoicePaymentStatus,
                invoiceId,
                paymentId,
                TypeUtil.stringToLocalDateTime(event.getCreatedAt()),
                changeId,
                sequenceId
        );
        log.info("Payment status has been mapped, sequenceId={}, changeId={}, invoiceId={}, paymentId={}, status={}",
                sequenceId, changeId, invoiceId, paymentId, invoicePaymentStatus.getSetField().getFieldName());
        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setKey(InvoicingKey.buildKey(invoiceId, paymentId));
        paymentWrapper.setPaymentStatusInfo(statusInfo);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
