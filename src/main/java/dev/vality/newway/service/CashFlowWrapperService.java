package dev.vality.newway.service;

import dev.vality.newway.dao.invoicing.iface.CashFlowDao;
import dev.vality.newway.dao.invoicing.iface.CashFlowLinkDao;
import dev.vality.newway.dao.invoicing.impl.CashFlowLinkIdsGeneratorDaoImpl;
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import dev.vality.newway.model.CashFlowWrapper;
import dev.vality.newway.model.InvoicingKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CashFlowWrapperService {

    private final CashFlowLinkDao cashFlowLinkDao;

    private final CashFlowDao cashFlowDao;

    private final CashFlowLinkIdsGeneratorDaoImpl idsGenerator;

    // TODO: что если в одной пачке придут события на создание и изменение cash flow?
    //  добавить sequence id?
    //  сортировать/группировать записи

    public void saveBatch(List<CashFlowWrapper> cashFlowWrappers) {
        if (!CollectionUtils.isEmpty(cashFlowWrappers)) {
            setLinkIds(cashFlowWrappers);
            List<CashFlowLink> links = cashFlowWrappers.stream()
                    .map(CashFlowWrapper::getCashFlowLink)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            cashFlowLinkDao.saveBatch(links);
            Set<InvoicingKey> invoicingKeys = links.stream()
                    .map(link -> InvoicingKey.buildKey(link.getInvoiceId(), link.getPaymentId()))
                    .collect(Collectors.toSet());
            cashFlowLinkDao.switchCurrent(invoicingKeys);
            List<CashFlow> cashFlows = cashFlowWrappers.stream()
                    .flatMap(wrapper -> wrapper.getCashFlows().stream())
                    .collect(Collectors.toList());
            cashFlowDao.save(cashFlows);
        }
    }

    private void setLinkIds(List<CashFlowWrapper> cashFlowWrappers) {
        Iterator<Long> linkIdIterator = idsGenerator.get(cashFlowWrappers.size()).iterator();
        for (CashFlowWrapper wrapper : cashFlowWrappers) {
            Long linkId = linkIdIterator.next();
            wrapper.getCashFlowLink().setId(linkId);
            for (CashFlow cashFlow : wrapper.getCashFlows()) {
                cashFlow.setObjId(linkId);
            }
        }
    }

}
