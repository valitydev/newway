package dev.vality.newway.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoicePaymentEventId {
    private final String invoiceId;
    private final String paymentId;
    private final LocalDateTime eventCreatedAt;
    private final Long sequenceId;
    private final Integer changeId;
}
