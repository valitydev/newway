package dev.vality.newway.service;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.model.InvoiceWrapper;
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
                .mapToObj(x -> new InvoiceWrapper(dev.vality.testcontainers.annotations.util.RandomBeans.random(Invoice.class, "id"),
                        dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(3, InvoiceCart.class, "id", "invId")))
                .collect(Collectors.toList());

        String invoiceIdFirst = "invoiceIdFirst";
        String invoiceIdSecond = "invoiceIdSecond";
        invoiceWrappers.get(0).getInvoice().setInvoiceId(invoiceIdFirst);
        invoiceWrappers.get(1).getInvoice().setInvoiceId(invoiceIdFirst);
        invoiceWrappers.get(2).getInvoice().setInvoiceId(invoiceIdSecond);
        invoiceWrappers.get(3).getInvoice().setInvoiceId(invoiceIdSecond);
        invoiceWrappers.forEach(iw -> iw.getInvoice().setCurrent(false));
        invoiceBatchService.process(invoiceWrappers);

        Invoice invoiceFirstGet = invoiceDao.get(invoiceIdFirst);
        Assertions.assertNotEquals(invoiceWrappers.get(0).getInvoice().getPartyId(), invoiceFirstGet.getPartyId());
        Assertions.assertEquals(invoiceWrappers.get(1).getInvoice().getPartyId(), invoiceFirstGet.getPartyId());

        Invoice invoiceSecondGet = invoiceDao.get(invoiceIdSecond);
        Assertions.assertNotEquals(invoiceWrappers.get(2).getInvoice().getShopId(), invoiceSecondGet.getShopId());
        Assertions.assertEquals(invoiceWrappers.get(3).getInvoice().getShopId(), invoiceSecondGet.getShopId());

        //Duplication check
        invoiceBatchService.process(invoiceWrappers);
        Assertions.assertEquals(2, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.invoice WHERE invoice_id = ? ", new Object[]{invoiceIdFirst},
                        Integer.class)).intValue());
        Assertions.assertEquals(2, Objects.requireNonNull(jdbcTemplate
                .queryForObject("SELECT count(*) FROM nw.invoice WHERE invoice_id = ? ", new Object[]{invoiceIdSecond},
                        Integer.class)).intValue());
        Assertions.assertEquals(3, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.invoice_cart where inv_id = ? ",
                new Object[]{invoiceFirstGet.getId()}, Integer.class)).intValue());
        Assertions.assertEquals(24, Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) FROM nw.invoice_cart ", Integer.class)).intValue());
    }
}
