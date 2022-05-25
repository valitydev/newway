package dev.vality.newway.model;

import lombok.Data;

@Data
public class InvoicePaymentEventIdHolder {
    private final String invoiceId;
    private final String paymentId;
    private final Long sequenceId;
    private final Integer changeId;
}
