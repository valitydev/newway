package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.InvoicePaymentCaptured;
import dev.vality.damsel.domain.InvoicePaymentStatus;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.enums.PaymentStatus;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.service.PaymentWrapperService;
import dev.vality.newway.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentStatusChangedMapper extends AbstractInvoicingPaymentMapper {

    private final PaymentWrapperService paymentWrapperService;

    private Filter filter = new PathConditionFilter(
            new PathConditionRule("invoice_payment_change.payload.invoice_payment_status_changed",
                    new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentStatus invoicePaymentStatus =
                change.getInvoicePaymentChange().getPayload().getInvoicePaymentStatusChanged().getStatus();
        long sequenceId = event.getEventId();
        String invoiceId = event.getSourceId();
        String paymentId = change.getInvoicePaymentChange().getId();

        log.info("Start payment status changed mapping, sequenceId={}, invoiceId={}, paymentId={}, status={}",
                sequenceId, invoiceId, paymentId, invoicePaymentStatus.getSetField().getFieldName());

        PaymentWrapper paymentWrapper = paymentWrapperService.get(invoiceId, paymentId, sequenceId, changeId, storage);
        if (paymentWrapper == null) {
            return null;
        }
        paymentWrapper.setShouldInsert(true);
        Payment paymentSource = paymentWrapper.getPayment();
        setInsertProperties(paymentSource, sequenceId, changeId, event.getCreatedAt());
        paymentSource.setStatus(TBaseUtil.unionFieldToEnum(invoicePaymentStatus, PaymentStatus.class));
        if (invoicePaymentStatus.isSetCancelled()) {
            paymentSource.setStatusCancelledReason(invoicePaymentStatus.getCancelled().getReason());
            paymentSource.setStatusCapturedReason(null);
            paymentSource.setStatusFailedFailure(null);
        } else if (invoicePaymentStatus.isSetCaptured()) {
            paymentSource.setStatusCancelledReason(null);
            InvoicePaymentCaptured invoicePaymentCaptured = invoicePaymentStatus.getCaptured();
            paymentSource.setStatusCapturedReason(invoicePaymentCaptured.getReason());
            if (invoicePaymentCaptured.isSetCost()) {
                paymentSource.setAmount(invoicePaymentCaptured.getCost().getAmount());
                paymentSource.setCurrencyCode(invoicePaymentCaptured.getCost().getCurrency().getSymbolicCode());
            }
            paymentSource.setStatusFailedFailure(null);
        } else if (invoicePaymentStatus.isSetFailed()) {
            paymentSource.setStatusCancelledReason(null);
            paymentSource.setStatusCapturedReason(null);
            paymentSource.setStatusFailedFailure(JsonUtil.thriftBaseToJsonString(invoicePaymentStatus.getFailed()));
        }
        log.info("Payment status has been mapped, sequenceId={}, invoiceId={}, paymentId={}, status={}",
                sequenceId, invoiceId, paymentId, invoicePaymentStatus.getSetField().getFieldName());
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
