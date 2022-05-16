package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.FinalCashFlowPosting;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.mapper.Mapper;
import dev.vality.newway.model.CashFlowWrapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.util.CashFlowLinkUtil;
import dev.vality.newway.util.CashFlowUtil;
import dev.vality.newway.util.PaymentFeeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentCashFlowChangedMapper implements Mapper<PaymentWrapper> {

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_cash_flow_changed",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        long sequenceId = event.getEventId();
        LocalDateTime eventCreatedAt = TypeUtil.stringToLocalDateTime(event.getCreatedAt());
        log.info("Start mapping payment cashflow change, sequenceId='{}', changeId='{}', invoiceId='{}', paymentId='{}'",
                sequenceId, changeId, invoiceId, paymentId);
        List<FinalCashFlowPosting> finalCashFlow =
                invoicePaymentChange.getPayload().getInvoicePaymentCashFlowChanged().getCashFlow();
        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setKey(InvoicingKey.buildKey(invoiceId, paymentId));
        paymentWrapper.setCashFlowWrapper(new CashFlowWrapper(
                CashFlowLinkUtil.getCashFlowLink(paymentId, invoiceId, eventCreatedAt, changeId, sequenceId),
                CashFlowUtil.convertCashFlows(finalCashFlow, null, PaymentChangeType.payment)
        ));
        paymentWrapper.setPaymentFee(
                PaymentFeeUtil.getPaymentFee(finalCashFlow, invoiceId, paymentId, eventCreatedAt, changeId, sequenceId));
        log.info("Payment cashflow has been mapped, sequenceId='{}', changeId='{}', invoiceId='{}', paymentId='{}'",
                sequenceId, changeId, invoiceId, paymentId);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }

}
