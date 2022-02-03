package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.RiskScore;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentChange;
import dev.vality.geck.common.util.TypeUtil;
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
public class InvoicePaymentRiskScoreChangedMapper extends AbstractInvoicingPaymentMapper {

    private final PaymentWrapperService paymentWrapperService;

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_risk_score_changed",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange change, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentChange invoicePaymentChange = change.getInvoicePaymentChange();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePaymentChange.getId();
        long sequenceId = event.getEventId();
        log.info("Start mapping payment risk score change, sequenceId='{}', invoiceId='{}', paymentId='{}'", sequenceId,
                invoiceId, paymentId);
        PaymentWrapper paymentWrapper = paymentWrapperService.get(invoiceId, paymentId, sequenceId, changeId, storage);
        if (paymentWrapper == null) {
            return null;
        }
        Payment paymentSource = paymentWrapper.getPayment();
        setUpdateProperties(paymentSource, event.getCreatedAt());
        RiskScore riskScore = invoicePaymentChange.getPayload().getInvoicePaymentRiskScoreChanged().getRiskScore();
        dev.vality.newway.domain.enums.RiskScore score =
                TypeUtil.toEnumField(riskScore.name(), dev.vality.newway.domain.enums.RiskScore.class);
        if (score == null) {
            throw new IllegalArgumentException("Illegal risk score: " + riskScore);
        }
        paymentSource.setRiskScore(score);
        log.info("Payment risk score have been mapped, sequenceId='{}', invoiceId='{}', paymentId='{}'", sequenceId,
                invoiceId, paymentId);
        return paymentWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
