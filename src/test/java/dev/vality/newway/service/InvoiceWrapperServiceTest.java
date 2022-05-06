package dev.vality.newway.service;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import dev.vality.newway.model.InvoiceWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PostgresqlSpringBootITest
public class InvoiceWrapperServiceTest {

    @Autowired
    private InvoiceWrapperService service;

    @Test
    public void testGet() {
        List<InvoiceWrapper> invoiceWrappers = IntStream.range(1, 5)
                .mapToObj(x -> new InvoiceWrapper(
                        dev.vality.testcontainers.annotations.util.RandomBeans.random(Invoice.class),
                        dev.vality.testcontainers.annotations.util.RandomBeans.random(InvoiceStatusInfo.class),
                        dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(3, InvoiceCart.class))
                )
                .collect(Collectors.toList());

        invoiceWrappers.forEach(iw -> {
            iw.getInvoiceStatusInfo().setInvoiceId(iw.getInvoice().getInvoiceId());
            iw.getCarts().forEach(c -> c.setInvoiceId(iw.getInvoice().getInvoiceId()));
        });
        service.save(invoiceWrappers);
    }
}
