package dev.vality.newway.mapper.payment;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentCaptureData;
import dev.vality.damsel.payment_processing.InvoicePaymentCaptureStarted;
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
import dev.vality.newway.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentCaptureStartedMapper extends AbstractInvoicingPaymentMapper {

    private final PaymentWrapperService paymentWrapperService;

    private final Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_capture_started",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();

        long sequenceId = event.getEventId();
        String paymentId = invoicePaymentChange.getId();
        String invoiceId = event.getSourceId();
        log.info("Start payment capture started handling, sequenceId={}, invoiceId={}, paymentId={}", sequenceId,
                invoiceId, paymentId);

        PaymentWrapper paymentWrapper = paymentWrapperService.get(invoiceId, paymentId, sequenceId, changeId, storage);
        if (paymentWrapper == null) {
            return null;
        }

        Payment paymentSource = paymentWrapper.getPayment();
        setUpdateProperties(paymentSource, event.getCreatedAt());
        InvoicePaymentCaptureStarted invoicePaymentCaptureStarted =
                change.getInvoicePaymentChange().getPayload().getInvoicePaymentCaptureStarted();
        InvoicePaymentCaptureData data = invoicePaymentCaptureStarted.getData();
        if (data.isSetCash()) {
            paymentSource.setAmount(data.getCash().getAmount());
            paymentSource.setCurrencyCode(data.getCash().getCurrency().getSymbolicCode());
        }
        paymentSource.setStatusCapturedStartedReason(data.getReason());
        if (data.isSetCart()) {
            String cartsJson = JsonUtil.objectToJsonString(
                    data.getCart().getLines().stream().map(JsonUtil::thriftBaseToJsonNode)
                            .collect(Collectors.toList()));
            paymentSource.setCaptureStartedParamsCartJson(cartsJson);
        }
        log.info("Payment has been saved, sequenceId={}, invoiceId={}, paymentId={}", sequenceId, invoiceId,
                paymentSource.getId());
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
