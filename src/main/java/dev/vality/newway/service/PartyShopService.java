package dev.vality.newway.service;

import com.github.benmanes.caffeine.cache.Cache;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.model.PartyShop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartyShopService {

    private final InvoiceDao invoiceDao;
    private final Cache<String, PartyShop> partyShopDataCache;

    public void put(String invoiceId, PartyShop partyShop) {
        partyShopDataCache.put(invoiceId, partyShop);
    }

    public PartyShop get(String invoiceId) {
        PartyShop partyShop = partyShopDataCache.getIfPresent(invoiceId);
        if (partyShop != null) {
            return new PartyShop(partyShop.getPartyId(), partyShop.getShopId());
        }
        Invoice invoice = invoiceDao.get(invoiceId);
        if (invoice == null) {
            return null;
        }
        partyShop = new PartyShop(invoice.getPartyId(), invoice.getShopId());
        partyShopDataCache.put(invoiceId, partyShop);
        return partyShop;
    }

}
