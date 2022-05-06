package dev.vality.newway.service;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.testcontainers.annotations.util.RandomBeans;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PostgresqlSpringBootITest
public class InvoiceBatchServiceTest {

    @Autowired
    private InvoiceBatchService invoiceBatchService;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void processTest() {
        List<InvoiceWrapper> invoiceWrappers = IntStream.range(1, 5)
                .mapToObj(x -> new InvoiceWrapper(
                        RandomBeans.random(Invoice.class, "id"),
                        RandomBeans.random(InvoiceStatusInfo.class, "id", "invoiceId"),
                        RandomBeans.randomListOf(3, InvoiceCart.class, "id", "invoiceId")))
                .collect(Collectors.toList());

        String invoiceIdFirst = invoiceWrappers.get(0).getInvoice().getInvoiceId();
        String invoiceIdSecond = invoiceWrappers.get(1).getInvoice().getInvoiceId();
        String invoiceIdThird = invoiceWrappers.get(2).getInvoice().getInvoiceId();
        String invoiceIdFourth = invoiceWrappers.get(3).getInvoice().getInvoiceId();
        invoiceWrappers.forEach(iw -> {
            iw.getInvoiceStatusInfo().setInvoiceId(iw.getInvoice().getInvoiceId());
            iw.getCarts().forEach(cart ->
                    cart.setInvoiceId(iw.getInvoice().getInvoiceId()));
        });
        invoiceBatchService.process(invoiceWrappers);

        Invoice invoiceFirstGet = invoiceDao.get(invoiceIdFirst);
        Assertions.assertEquals(invoiceWrappers.get(0).getInvoice().getPartyId(), invoiceFirstGet.getPartyId());
        Assertions.assertNotEquals(invoiceWrappers.get(1).getInvoice().getPartyId(), invoiceFirstGet.getPartyId());

        Invoice invoiceThirdGet = invoiceDao.get(invoiceIdThird);
        Assertions.assertEquals(invoiceWrappers.get(2).getInvoice().getShopId(), invoiceThirdGet.getShopId());
        Assertions.assertNotEquals(invoiceWrappers.get(3).getInvoice().getShopId(), invoiceThirdGet.getShopId());

        //Duplication check
        invoiceBatchService.process(invoiceWrappers);
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.invoice WHERE invoice_id = ? ", new Object[]{invoiceIdFirst},
                        Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.invoice WHERE invoice_id = ? ", new Object[]{invoiceIdSecond},
                        Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.invoice WHERE invoice_id = ? ", new Object[]{invoiceIdThird},
                        Integer.class)).intValue());
        Assertions.assertEquals(1, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.invoice WHERE invoice_id = ? ", new Object[]{invoiceIdFourth},
                        Integer.class)).intValue());

        Assertions.assertEquals(
                3,
                Objects.requireNonNull(
                        jdbcTemplate.queryForObject(
                                "SELECT count(*) FROM nw.invoice_cart where invoice_id = ? ",
                                new Object[]{invoiceFirstGet.getInvoiceId()},
                                Integer.class
                        ))
        );
        Assertions.assertEquals(12, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.invoice_cart ", Integer.class)).intValue());
    }
}
