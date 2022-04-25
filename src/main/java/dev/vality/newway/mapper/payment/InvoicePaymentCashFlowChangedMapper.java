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
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.mapper.AbstractInvoicingMapper;
import dev.vality.newway.model.PaymentWrapper;
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
public class InvoicePaymentCashFlowChangedMapper extends AbstractInvoicingMapper<PaymentWrapper> {

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_cash_flow_changed",
            new IsNullCondition().not()));
    // TODO
    //  adjustment - корректировка, чаще все корректируют комиссии
    //  при приходе adjustment - приходит новый кешфлоу(првоерить) и нужно пересчитать fee?

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        long sequenceId = event.getEventId();
        LocalDateTime eventCreatedAt = TypeUtil.stringToLocalDateTime(event.getCreatedAt());
        log.info("Start mapping payment cashflow change, sequenceId='{}', invoiceId='{}', paymentId='{}'",
                sequenceId, invoiceId, paymentId);
        List<FinalCashFlowPosting> finalCashFlow =
                invoicePaymentChange.getPayload().getInvoicePaymentCashFlowChanged().getCashFlow();
        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setCashFlows(
                CashFlowUtil.convertCashFlows(finalCashFlow, null, PaymentChangeType.payment));
        paymentWrapper.setPaymentFee(
                PaymentFeeUtil.getPaymentFee(finalCashFlow, invoiceId, paymentId, eventCreatedAt, changeId, sequenceId));
        log.info("Payment cashflow has been mapped, sequenceId='{}', invoiceId='{}', paymentId='{}'",
                sequenceId, invoiceId, paymentId);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }

}
