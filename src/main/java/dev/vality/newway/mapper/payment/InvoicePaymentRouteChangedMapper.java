package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.PaymentRoute;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.service.PaymentWrapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentRouteChangedMapper extends AbstractInvoicingPaymentMapper {

    private final PaymentWrapperService paymentWrapperService;

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_route_changed",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        PaymentRoute paymentRoute = invoicePaymentChange.getPayload().getInvoicePaymentRouteChanged().getRoute();
        long sequenceId = event.getEventId();
        log.info("Start mapping payment route change, route='{}', sequenceId='{}', invoiceId='{}', paymentId='{}'",
                paymentRoute, sequenceId, invoiceId, paymentId);
        PaymentWrapper paymentWrapper = paymentWrapperService.get(invoiceId, paymentId, sequenceId, changeId, storage);
        if (paymentWrapper == null) {
            return null;
        }
        Payment paymentSource = paymentWrapper.getPayment();
        setUpdateProperties(paymentSource, event.getCreatedAt());
        paymentSource.setRouteProviderId(paymentRoute.getProvider().getId());
        paymentSource.setRouteTerminalId(paymentRoute.getTerminal().getId());
        log.info("Payment route have been mapped, route='{}', sequenceId='{}', invoiceId='{}', paymentId='{}'",
                paymentRoute, sequenceId, invoiceId, paymentId);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
