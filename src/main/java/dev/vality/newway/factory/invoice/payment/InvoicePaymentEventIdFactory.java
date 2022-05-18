package dev.vality.newway.factory.invoice.payment;

import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import dev.vality.newway.model.InvoicePaymentEventId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoicePaymentEventIdFactory {

    public static InvoicePaymentEventId build(CashFlowLink link) {
        return new InvoicePaymentEventId(
                link.getInvoiceId(),
                link.getPaymentId(),
                link.getEventCreatedAt(),
                link.getSequenceId(),
                link.getChangeId()
        );
    }

}
