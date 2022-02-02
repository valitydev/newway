package dev.vality.newway.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaymentWrapperTest {

    @Test
    public void copyTest() {
        PaymentWrapper paymentWrapper = dev.vality.testcontainers.annotations.util.RandomBeans.random(PaymentWrapper.class);
        PaymentWrapper copy = paymentWrapper.copy();
        paymentWrapper.getPayment().setInvoiceId("kek");
        paymentWrapper.getCashFlows().get(0).setObjId(124L);
        Assertions.assertNotEquals(paymentWrapper.getPayment().getInvoiceId(), copy.getPayment().getInvoiceId());
        Assertions.assertNotEquals(paymentWrapper.getCashFlows().get(0).getObjId(), copy.getCashFlows().get(0).getObjId());
    }
}
