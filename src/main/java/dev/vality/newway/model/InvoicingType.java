package dev.vality.newway.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvoicingType {
    INVOICE("invoice"),
    PAYMENT("payment");

    private String value;
}
