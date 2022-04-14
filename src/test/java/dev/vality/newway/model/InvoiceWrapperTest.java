package dev.vality.newway.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvoiceWrapperTest {

    @Test
    public void copyTest() {
        InvoiceWrapper invoiceWrapper = dev.vality.testcontainers.annotations.util.RandomBeans.random(InvoiceWrapper.class);
        InvoiceWrapper copy = invoiceWrapper.copy();
        invoiceWrapper.getInvoice().setInvoiceId("kek");
        invoiceWrapper.getCarts().get(0).setInvoiceId("kek");
        Assertions.assertNotEquals(invoiceWrapper.getInvoice().getInvoiceId(), copy.getInvoice().getInvoiceId());
        Assertions.assertNotEquals(
                invoiceWrapper.getCarts().get(0).getInvoiceId(),
                copy.getCarts().get(0).getInvoiceId()
        );
    }
}
