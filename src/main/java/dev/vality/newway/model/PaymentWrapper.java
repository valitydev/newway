package dev.vality.newway.model;

import dev.vality.newway.domain.tables.pojos.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private CashFlowWrapper cashFlowWrapper;
    private InvoicingKey key;
}
