package dev.vality.newway.service;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.handler.event.stock.LocalStorage;
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
                .mapToObj(x -> new InvoiceWrapper(dev.vality.testcontainers.annotations.util.RandomBeans.random(Invoice.class), dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(3, InvoiceCart.class)))
                .collect(Collectors.toList());

        invoiceWrappers.forEach(iw -> {
            iw.getInvoice().setCurrent(false);
            iw.getCarts().forEach(c -> c.setInvId(iw.getInvoice().getId()));
        });
        service.save(invoiceWrappers);

        InvoiceWrapper invoiceWrapper = service.get(invoiceWrappers.get(0).getInvoice().getInvoiceId(),
                new LocalStorage());
        Assertions.assertEquals(invoiceWrappers.get(0).getInvoice().getShopId(), invoiceWrapper.getInvoice().getShopId());
    }
}
