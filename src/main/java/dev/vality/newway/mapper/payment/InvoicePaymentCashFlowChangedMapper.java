package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.FinalCashFlowPosting;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.service.PaymentWrapperService;
import dev.vality.newway.util.CashFlowType;
import dev.vality.newway.util.CashFlowUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentCashFlowChangedMapper extends AbstractInvoicingPaymentMapper {

    private final PaymentWrapperService paymentWrapperService;

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_cash_flow_changed",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        long sequenceId = event.getEventId();
        log.info("Start mapping payment cashflow change, sequenceId='{}', invoiceId='{}', paymentId='{}'", sequenceId,
                invoiceId, paymentId);
        PaymentWrapper paymentWrapper = paymentWrapperService.get(invoiceId, paymentId, sequenceId, changeId, storage);
        if (paymentWrapper == null) {
            return null;
        }
        paymentWrapper.setShouldInsert(true);
        Payment paymentSource = paymentWrapper.getPayment();
        setInsertProperties(paymentSource, sequenceId, changeId, event.getCreatedAt());
        List<FinalCashFlowPosting> finalCashFlow =
                invoicePaymentChange.getPayload().getInvoicePaymentCashFlowChanged().getCashFlow();
        List<CashFlow> cashFlows = CashFlowUtil.convertCashFlows(finalCashFlow, null, PaymentChangeType.payment);
        paymentWrapper.setCashFlows(cashFlows);
        Map<CashFlowType, Long> parsedCashFlow = CashFlowUtil.parseCashFlow(finalCashFlow);
        paymentSource.setFee(parsedCashFlow.getOrDefault(CashFlowType.FEE, 0L));
        paymentSource.setProviderFee(parsedCashFlow.getOrDefault(CashFlowType.PROVIDER_FEE, 0L));
        paymentSource.setExternalFee(parsedCashFlow.getOrDefault(CashFlowType.EXTERNAL_FEE, 0L));
        paymentSource.setGuaranteeDeposit(parsedCashFlow.getOrDefault(CashFlowType.GUARANTEE_DEPOSIT, 0L));
        log.info("Payment cashflow has been mapped, sequenceId='{}', invoiceId='{}', paymentId='{}'", sequenceId,
                invoiceId, paymentId);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
