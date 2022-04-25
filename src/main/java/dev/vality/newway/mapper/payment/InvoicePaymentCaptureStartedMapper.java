package dev.vality.newway.mapper.payment;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.mapper.Mapper;
import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * We do not save InvoicePaymentCaptureStarted because it is first commit in two-phase transaction -
 * processed -> captured status transition.
 * InvoicePaymentCaptureStarted payload duplicates InvoicePaymentStatusChanged event
 * with InvoicePaymentCaptured payload.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentCaptureStartedMapper implements Mapper<PaymentWrapper> {

    private final Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_capture_started",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId) {
        log.info("Receive InvoicePaymentCaptureStarted sequenceId={}, invoiceId={}, paymentId={}, payload={}",
                event.getEventId(),
                change.getInvoicePaymentChange().getId(),
                event.getSourceId(),
                change.getInvoicePaymentChange().getPayload().getInvoicePaymentCaptureStarted()
        );
        return new PaymentWrapper();
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
