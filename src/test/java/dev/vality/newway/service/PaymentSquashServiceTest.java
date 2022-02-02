package dev.vality.newway.service;

import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PaymentWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PaymentSquashServiceTest {

    @Test
    public void squashSimpleTest() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper paymentWrapper = buildPaymentWrapper("1", 666L, true);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(paymentWrapper), List.of(1L));
        Assertions.assertEquals(1, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
    }

    @Test
    public void squashSimple0Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper paymentWrapper = buildPaymentWrapper("1", 666L, false);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(paymentWrapper), List.of(1L));
        Assertions.assertEquals(1, squashedWrappers.size());
        Assertions.assertFalse(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 666);
    }

    @Test
    public void squashSimple1Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, true);
        PaymentWrapper pw2 = buildPaymentWrapper("inv_id", "1", 667L, 2L, false);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(pw1, pw2), List.of(1L, 2L));
        Assertions.assertEquals(1, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getPartyRevision(), pw2.getPayment().getPartyRevision());
    }

    @Test
    public void squashSimple11Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, false);
        PaymentWrapper pw2 = buildPaymentWrapper("inv_id", "1", 667L, 2L, false);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(pw1, pw2), List.of(1L, 2L));
        Assertions.assertEquals(1, squashedWrappers.size());
        Assertions.assertFalse(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 666L);
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getPartyRevision(), pw2.getPayment().getPartyRevision());
    }

    @Test
    public void squashSimple12Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, true);
        PaymentWrapper pw2 = buildPaymentWrapper("inv_id", "1", 667L, 2L, true);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(pw1, pw2), List.of(1L, 2L));
        Assertions.assertEquals(2, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getPartyRevision().longValue(), 0);
        Assertions.assertTrue(squashedWrappers.get(1).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getId().longValue(), 2);
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getPartyRevision(), pw2.getPayment().getPartyRevision());
    }

    @Test
    public void squashSimple2Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, false);
        PaymentWrapper pw2 = buildPaymentWrapper("inv_id", "1", 667L, 3L, true);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(pw1, pw2), List.of(1L, 2L));
        Assertions.assertEquals(2, squashedWrappers.size());
        Assertions.assertFalse(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 666);
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getPartyRevision().longValue(), 0);
        Assertions.assertTrue(squashedWrappers.get(1).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getId().longValue(), 1);
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getPartyRevision().longValue(), 3L);
    }

    @Test
    public void squashSimple3Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, true);
        PaymentWrapper pw2 = buildPaymentWrapper("2", 667L, false);
        List<PaymentWrapper> squashedWrappers = service.squash(List.of(pw1, pw2), List.of(1L, 2L));
        Assertions.assertEquals(2, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
        Assertions.assertFalse(squashedWrappers.get(1).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getId().longValue(), 667);
    }

    @Test
    public void squashSimple4Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, true);
        PaymentWrapper pw2 = buildPaymentWrapper("1", 667L, false);
        PaymentWrapper pw3 = buildPaymentWrapper("1", 668L, false);
        PaymentWrapper pw4 = buildPaymentWrapper("1", 669L, true);
        PaymentWrapper pw5 = buildPaymentWrapper("1", 670L, false);
        List<PaymentWrapper> squashedWrappers =
                service.squash(List.of(pw1, pw2, pw3, pw4, pw5), List.of(1L, 2L, 3L, 4L, 5L));
        Assertions.assertEquals(2, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
        Assertions.assertTrue(squashedWrappers.get(1).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getId().longValue(), 2);
    }

    @Test
    public void squashSimple41Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, true);
        PaymentWrapper pw2 = buildPaymentWrapper("1", 667L, false);
        PaymentWrapper pw3 = buildPaymentWrapper("2", 668L, false);
        PaymentWrapper pw4 = buildPaymentWrapper("2", 669L, true);
        PaymentWrapper pw5 = buildPaymentWrapper("inv_id", "2", 670L, 2L, false);
        List<PaymentWrapper> squashedWrappers =
                service.squash(List.of(pw1, pw2, pw3, pw4, pw5), List.of(1L, 2L, 3L, 4L, 5L));
        Assertions.assertEquals(3, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
        Assertions.assertFalse(squashedWrappers.get(1).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getId().longValue(), 668);
        Assertions.assertTrue(squashedWrappers.get(2).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(2).getPayment().getId().longValue(), 2);
        Assertions.assertEquals(squashedWrappers.get(2).getPayment().getPaymentId(), "2");
        Assertions.assertEquals(squashedWrappers.get(2).getPayment().getPartyRevision(), pw5.getPayment().getPartyRevision());

    }


    @Test
    public void squashSimple42Test() {
        PaymentSquashService service = new PaymentSquashService();
        PaymentWrapper pw1 = buildPaymentWrapper("1", 666L, true);
        PaymentWrapper pw2 = buildPaymentWrapper("1", 667L, false);
        PaymentWrapper pw3 = buildPaymentWrapper("2", 668L, true);
        PaymentWrapper pw4 = buildPaymentWrapper("2", 669L, false);
        PaymentWrapper pw5 = buildPaymentWrapper("1", 670L, false);
        List<PaymentWrapper> squashedWrappers =
                service.squash(List.of(pw1, pw2, pw3, pw4, pw5), List.of(1L, 2L, 3L, 4L, 5L));
        Assertions.assertEquals(2, squashedWrappers.size());
        Assertions.assertTrue(squashedWrappers.get(0).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(0).getPayment().getId().longValue(), 1);
        Assertions.assertTrue(squashedWrappers.get(1).isShouldInsert());
        Assertions.assertEquals(squashedWrappers.get(1).getPayment().getId().longValue(), 2);
    }

    private PaymentWrapper buildPaymentWrapper(String invoiceId, String paymentId, Long id, Long partyRevision,
                                               boolean isShouldInsert) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setInvoiceId(invoiceId);
        payment.setPaymentId(paymentId);
        payment.setPartyRevision(partyRevision);
        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setPayment(payment);
        paymentWrapper.setCashFlows(List.of(new CashFlow()));
        paymentWrapper.setKey(InvoicingKey.buildKey(invoiceId, paymentId));
        paymentWrapper.setShouldInsert(isShouldInsert);
        return paymentWrapper;
    }

    private PaymentWrapper buildPaymentWrapper(String paymentId, Long id, boolean isShouldInsert) {
        return buildPaymentWrapper("inv_id", paymentId, id, 0L, isShouldInsert);
    }
}
