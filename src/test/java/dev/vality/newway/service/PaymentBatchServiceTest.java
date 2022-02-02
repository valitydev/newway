package dev.vality.newway.service;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.invoicing.impl.PaymentDaoImpl;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PaymentWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PostgresqlSpringBootITest
public class PaymentBatchServiceTest {

    @Autowired
    private PaymentBatchService paymentBatchService;

    @Autowired
    private PaymentDaoImpl paymentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void processTest() {
        List<PaymentWrapper> paymentWrappers = IntStream.range(1, 5)
                .mapToObj(x -> new PaymentWrapper(
                        dev.vality.testcontainers.annotations.util.RandomBeans.random(Payment.class, "id"),
                        dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(3, CashFlow.class, "id", "objId"),
                        true,
                        null))
                .collect(Collectors.toList());

        String invoiceIdFirst = "invoiceIdFirst";
        String invoiceIdSecond = "invoiceIdSecond";
        paymentWrappers.get(0).getPayment().setInvoiceId(invoiceIdFirst);
        paymentWrappers.get(0).getPayment().setPaymentId("1");
        paymentWrappers.get(1).getPayment().setInvoiceId(invoiceIdFirst);
        paymentWrappers.get(1).getPayment().setPaymentId("1");
        paymentWrappers.get(2).getPayment().setInvoiceId(invoiceIdFirst);
        paymentWrappers.get(2).getPayment().setPaymentId("2");
        paymentWrappers.get(3).getPayment().setInvoiceId(invoiceIdSecond);
        paymentWrappers.get(3).getPayment().setPaymentId("1");
        paymentWrappers.forEach(iw -> {
            iw.setKey(InvoicingKey.buildKey(iw));
            iw.getPayment().setCurrent(false);
            iw.getCashFlows().forEach(c -> c.setObjType(PaymentChangeType.payment));
        });
        paymentBatchService.process(paymentWrappers);

        Payment paymentFirstGet = paymentDao.get(invoiceIdFirst, "1");
        Assertions.assertNotEquals(paymentWrappers.get(0).getPayment().getPartyId(), paymentFirstGet.getPartyId());
        Assertions.assertEquals(paymentWrappers.get(1).getPayment().getPartyId(), paymentFirstGet.getPartyId());

        Payment paymentSecondGet = paymentDao.get(invoiceIdFirst, "2");
        Assertions.assertEquals(paymentWrappers.get(2).getPayment().getShopId(), paymentSecondGet.getShopId());

        //Duplication check
        paymentBatchService.process(paymentWrappers);
        Payment paymentFirstGet2 = paymentDao.get(invoiceIdFirst, "1");
        Assertions.assertEquals(paymentWrappers.get(1).getPayment().getPartyId(), paymentFirstGet2.getPartyId());
        Assertions.assertEquals(2, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.payment WHERE invoice_id = ? and payment_id = ? ",
                        new Object[]{invoiceIdFirst, "1"}, Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.payment WHERE invoice_id = ? and payment_id = ? ",
                        new Object[]{invoiceIdFirst, "2"}, Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.payment WHERE invoice_id = ? and payment_id = ? ",
                        new Object[]{invoiceIdSecond, "1"}, Integer.class)).intValue());
        Assertions.assertEquals(24, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.cash_flow ", Integer.class)).intValue());
    }
}
