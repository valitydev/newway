package dev.vality.newway.service;

import com.github.benmanes.caffeine.cache.Cache;
import dev.vality.newway.dao.invoicing.iface.InvoiceCartDao;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.InvoicingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceWrapperService {

    private final InvoiceDao invoiceDao;
    private final InvoiceCartDao invoiceCartDao;
    private final Cache<InvoicingKey, InvoiceWrapper> invoiceDataCache;

    public InvoiceWrapper get(String invoiceId, LocalStorage storage) throws DaoException, NotFoundException {
        InvoicingKey key = InvoicingKey.builder().invoiceId(invoiceId).type(InvoicingType.INVOICE).build();
        InvoiceWrapper invoiceWrapper = (InvoiceWrapper) storage.get(key);
        if (invoiceWrapper != null) {
            return invoiceWrapper.copy();
        }
        invoiceWrapper = invoiceDataCache.getIfPresent(key);
        if (invoiceWrapper != null) {
            return invoiceWrapper.copy();
        }
        Invoice invoice = invoiceDao.get(invoiceId);
        List<InvoiceCart> carts = invoiceCartDao.getByInvId(invoice.getId());
        return new InvoiceWrapper(invoice, carts);
    }

    public void save(List<InvoiceWrapper> invoiceWrappers) {
        invoiceWrappers.forEach(i -> invoiceDataCache
                .put(InvoicingKey.builder().invoiceId(i.getInvoice().getInvoiceId()).type(InvoicingType.INVOICE)
                        .build(), i));
        List<Invoice> invoices = invoiceWrappers.stream().map(InvoiceWrapper::getInvoice).collect(Collectors.toList());
        invoiceDao.saveBatch(invoices);
        List<InvoiceCart> carts =
                invoiceWrappers.stream().filter(i -> i.getCarts() != null).map(InvoiceWrapper::getCarts)
                        .flatMap(Collection::stream).collect(Collectors.toList());
        invoiceCartDao.save(carts);
    }
}
