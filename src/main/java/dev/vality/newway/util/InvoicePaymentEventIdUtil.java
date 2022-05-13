package dev.vality.newway.util;

import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import dev.vality.newway.model.InvoicePaymentEventId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoicePaymentEventIdUtil {

    public static InvoicePaymentEventId get(CashFlowLink link) {
        return new InvoicePaymentEventId(
                link.getInvoiceId(),
                link.getPaymentId(),
                link.getEventCreatedAt(),
                link.getSequenceId(),
                link.getChangeId()
        );
    }

}
