package dev.vality.newway.service;

import dev.vality.newway.dao.invoicing.iface.InvoiceCartDao;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.dao.invoicing.iface.InvoiceStatusInfoDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import dev.vality.newway.model.InvoiceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceWrapperService {

    private final InvoiceDao invoiceDao;
    private final InvoiceStatusInfoDao invoiceStatusInfoDao;
    private final InvoiceCartDao invoiceCartDao;

    public void save(List<InvoiceWrapper> invoiceWrappers) {
        saveInvoices(invoiceWrappers);
        saveStatusInfos(invoiceWrappers);
        saveCarts(invoiceWrappers);
        switchCurrent(invoiceWrappers);
    }

    private void saveInvoices(List<InvoiceWrapper> invoiceWrappers) {
        List<Invoice> invoices = invoiceWrappers.stream()
                .map(InvoiceWrapper::getInvoice)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!invoices.isEmpty()) {
            invoiceDao.saveBatch(invoices);
        }

    }

    private void saveStatusInfos(List<InvoiceWrapper> invoiceWrappers) {
        List<InvoiceStatusInfo> invoiceStatusInfos = invoiceWrappers.stream()
                .map(InvoiceWrapper::getInvoiceStatusInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!invoiceStatusInfos.isEmpty()) {
            invoiceStatusInfoDao.saveBatch(invoiceStatusInfos);
        }
    }

    private void saveCarts(List<InvoiceWrapper> invoiceWrappers) {
        List<InvoiceCart> carts =
                invoiceWrappers.stream()
                        .map(InvoiceWrapper::getCarts)
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        if (!carts.isEmpty()) {
            Set<String> invoiceIds = carts.stream()
                    .map(InvoiceCart::getInvoiceId)
                    .collect(Collectors.toSet());
            Set<String> existingInvoiceIds = invoiceCartDao.getExistingInvoiceIds(invoiceIds);
            carts.removeIf(cart -> existingInvoiceIds.contains(cart.getInvoiceId()));
            if (!carts.isEmpty()) {
                invoiceCartDao.save(carts);
            }
        }
    }

    private void switchCurrent(List<InvoiceWrapper> invoiceWrappers) {
        Set<String> invoiceStatusInfoChangedIds = invoiceWrappers.stream()
                .map(InvoiceWrapper::getInvoiceStatusInfo)
                .filter(Objects::nonNull)
                .map(InvoiceStatusInfo::getInvoiceId)
                .collect(Collectors.toSet());
        log.info("Switch to current ids - invoiceStatusInfo:{}", invoiceStatusInfoChangedIds);
        invoiceStatusInfoDao.switchCurrent(invoiceStatusInfoChangedIds);
    }

}
