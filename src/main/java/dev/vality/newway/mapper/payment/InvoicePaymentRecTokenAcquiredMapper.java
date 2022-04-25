package dev.vality.newway.mapper.payment;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.PaymentRecurrentInfo;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.mapper.AbstractInvoicingMapper;
import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentRecTokenAcquiredMapper extends AbstractInvoicingMapper<PaymentWrapper> {

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_rec_token_acquired",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        long sequenceId = event.getEventId();
        log.info("Start handling payment recurrent token acquired, sequenceId='{}', invoiceId='{}', paymentId='{}'",
                sequenceId, invoiceId, paymentId);
        PaymentRecurrentInfo recurrentInfo = new PaymentRecurrentInfo();
        recurrentInfo.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        recurrentInfo.setInvoiceId(invoiceId);
        recurrentInfo.setPaymentId(paymentId);
        recurrentInfo.setSequenceId(sequenceId);
        recurrentInfo.setChangeId(changeId);
        recurrentInfo.setToken(invoicePaymentChange.getPayload().getInvoicePaymentRecTokenAcquired().getToken());
        log.info("Payment recurrent token have been saved, sequenceId='{}', invoiceId='{}', paymentId='{}'", sequenceId,
                invoiceId, paymentId);
        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setPaymentRecurrentInfo(recurrentInfo);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
