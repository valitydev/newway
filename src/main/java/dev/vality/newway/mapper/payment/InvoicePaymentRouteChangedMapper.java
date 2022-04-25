package dev.vality.newway.mapper.payment;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.PaymentRoute;
import dev.vality.newway.mapper.Mapper;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.util.PaymentRouteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentRouteChangedMapper implements Mapper<PaymentWrapper> {

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_route_changed",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        dev.vality.damsel.domain.PaymentRoute paymentRouteSource =
                invoicePaymentChange.getPayload().getInvoicePaymentRouteChanged().getRoute();
        long sequenceId = event.getEventId();
        log.info("Start mapping payment route change, route='{}', sequenceId='{}', invoiceId='{}', paymentId='{}'",
                paymentRouteSource, sequenceId, invoiceId, paymentId);
        PaymentRoute paymentRoute = PaymentRouteUtil.getPaymentRoute(
                paymentRouteSource,
                invoiceId,
                paymentId,
                TypeUtil.stringToLocalDateTime(event.getCreatedAt()),
                changeId,
                sequenceId
        );
        log.info("Payment route have been mapped, route='{}', sequenceId='{}', invoiceId='{}', paymentId='{}'",
                paymentRoute, sequenceId, invoiceId, paymentId);
        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setPaymentRoute(paymentRoute);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
