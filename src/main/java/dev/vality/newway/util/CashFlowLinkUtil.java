package dev.vality.newway.util;

import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CashFlowLinkUtil {

    public static CashFlowLink getCashFlowLink(String paymentId,
                                         String invoiceId,
                                         LocalDateTime eventCreatedAt,
                                         Integer changeId,
                                         Long sequenceId) {
        CashFlowLink link = new CashFlowLink();
        link.setPaymentId(paymentId);
        link.setInvoiceId(invoiceId);
        link.setEventCreatedAt(eventCreatedAt);
        link.setChangeId(changeId);
        link.setSequenceId(sequenceId);
        return link;
    }

}
