package dev.vality.newway.model;

import dev.vality.newway.domain.tables.pojos.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentWrapper {
    private Payment payment;
    private PaymentStatusInfo paymentStatusInfo;
    private PaymentPayerInfo paymentPayerInfo;
    private PaymentAdditionalInfo paymentAdditionalInfo;
    private PaymentRecurrentInfo paymentRecurrentInfo;
    private PaymentRiskData paymentRiskData;
    private PaymentFee paymentFee;
    private PaymentRoute paymentRoute;
    private List<CashFlow> cashFlows;
    // TODO: check if required
    private InvoicingKey key;

    public PaymentWrapper copy() {
        return new PaymentWrapper(
                new Payment(payment),
                new PaymentStatusInfo(paymentStatusInfo),
                new PaymentPayerInfo(paymentPayerInfo),
                new PaymentAdditionalInfo(paymentAdditionalInfo),
                new PaymentRecurrentInfo(paymentRecurrentInfo),
                new PaymentRiskData(paymentRiskData),
                new PaymentFee(paymentFee),
                new PaymentRoute(paymentRoute),
                cashFlows != null ? copyCashFlow() : null,
                InvoicingKey.buildKey(this)
        );
    }

    private List<CashFlow> copyCashFlow() {
        return cashFlows.stream()
                .map(CashFlow::new)
                .collect(Collectors.toList());
    }
}
