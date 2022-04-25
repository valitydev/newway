package dev.vality.newway.service;

import com.github.benmanes.caffeine.cache.Cache;
import dev.vality.newway.dao.invoicing.iface.InvoiceCartDao;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.dao.invoicing.iface.InvoiceStatusInfoDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.util.InvoiceWrapperUtil;
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
    // TODO: is it required? Get stats for later
    private final Cache<InvoicingKey, InvoiceWrapper> invoiceDataCache;

    // TODO: refactor, required only for PartyShop, which can be extracted from
    public InvoiceWrapper get(String invoiceId) throws DaoException, NotFoundException {
        InvoicingKey key = InvoicingKey.buildKey(invoiceId);
        InvoiceWrapper invoiceWrapper = invoiceDataCache.getIfPresent(key);
        if (invoiceWrapper != null) {
            return invoiceWrapper.copy();
        }
        return getInvoiceWrapperFromDB(invoiceId);
    }

    public void save(List<InvoiceWrapper> invoiceWrappers) {
        saveInvoices(invoiceWrappers);
        saveStatusInfos(invoiceWrappers);
        saveCarts(invoiceWrappers);
        switchCurrent(invoiceWrappers);
        refreshCache(invoiceWrappers);
    }

    private InvoiceWrapper getInvoiceWrapperFromDB(String invoiceId) {
        Invoice invoice = invoiceDao.get(invoiceId);
        InvoiceStatusInfo invoiceStatusInfo = invoiceStatusInfoDao.get(invoiceId);
        List<InvoiceCart> carts = invoiceCartDao.getByInvoiceId(invoiceId);
        var wrapper = new InvoiceWrapper(invoice, invoiceStatusInfo, carts);
        invoiceDataCache.put(InvoicingKey.buildKey(invoiceId), wrapper);
        return wrapper;
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
            invoiceCartDao.save(carts);
        }
    }

    private void switchCurrent(List<InvoiceWrapper> invoiceWrappers) {
        Set<String> invoiceChangedIds = new HashSet<>();
        Set<String> invoiceStatusInfoChangedIds = new HashSet<>();
        for (InvoiceWrapper wrapper : invoiceWrappers) {
            if (wrapper.getInvoice() != null) {
                invoiceChangedIds.add(wrapper.getInvoice().getInvoiceId());
            }
            if (wrapper.getInvoiceStatusInfo() != null) {
                invoiceStatusInfoChangedIds.add(wrapper.getInvoiceStatusInfo().getInvoiceId());
            }
        }

        log.info("Switch to current ids - invoice:{}, invoiceStatusInfo:{}",
                invoiceChangedIds, invoiceStatusInfoChangedIds);
        invoiceDao.switchCurrent(invoiceChangedIds);
        invoiceStatusInfoDao.switchCurrent(invoiceStatusInfoChangedIds);
    }

    private void refreshCache(List<InvoiceWrapper> invoiceWrappers) {
        Set<String> invoiceIds = invoiceWrappers.stream()
                .map(InvoiceWrapperUtil::getInvoiceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        getBatchFromDB(invoiceIds).forEach(wrapper ->
                invoiceDataCache.put(InvoicingKey.buildKey(wrapper.getInvoice().getInvoiceId()), wrapper)
        );
    }

    private List<InvoiceWrapper> getBatchFromDB(Set<String> invoiceIds) {
        Map<String, Invoice> invoices = invoiceDao.getList(invoiceIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Invoice::getInvoiceId, invoice -> invoice));
        Map<String, InvoiceStatusInfo> invoiceStatusInfos = invoiceStatusInfoDao.getList(invoiceIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(InvoiceStatusInfo::getInvoiceId, statusInfo -> statusInfo));
        Map<String, List<InvoiceCart>> invoiceCarts = invoiceCartDao.getByInvoiceIdsIn(invoiceIds).stream()
                .collect(Collectors.groupingBy(InvoiceCart::getInvoiceId));
        return invoiceIds.stream()
                .map(invoiceId -> new InvoiceWrapper(
                        invoices.get(invoiceId),
                        invoiceStatusInfos.get(invoiceId),
                        invoiceCarts.get(invoiceId)
                ))
                .collect(Collectors.toList());
    }
}
