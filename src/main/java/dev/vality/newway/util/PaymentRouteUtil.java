package dev.vality.newway.util;

import dev.vality.newway.domain.tables.pojos.PaymentRoute;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRouteUtil {

    public static PaymentRoute getPaymentRoute(dev.vality.damsel.domain.PaymentRoute paymentRouteSource,
                                               String invoiceId,
                                               String paymentId,
                                               LocalDateTime eventCreatedAt,
                                               Integer changeId,
                                               Long sequenceId) {
        PaymentRoute paymentRoute = new PaymentRoute();
        paymentRoute.setEventCreatedAt(eventCreatedAt);
        paymentRoute.setInvoiceId(invoiceId);
        paymentRoute.setPaymentId(paymentId);
        paymentRoute.setRouteProviderId(paymentRouteSource.getProvider().getId());
        paymentRoute.setRouteTerminalId(paymentRouteSource.getTerminal().getId());
        paymentRoute.setSequenceId(sequenceId);
        paymentRoute.setChangeId(changeId);
        return paymentRoute;
    }

}
