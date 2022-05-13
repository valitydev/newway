package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.model.InvoicePaymentEventId;
import dev.vality.newway.model.InvoicingKey;

import java.util.List;
import java.util.Set;

public interface CashFlowLinkDao extends GenericDao {

    void saveBatch(List<CashFlowLink> links) throws DaoException;

    CashFlowLink get(String invoiceId, String paymentId);

    void switchCurrent(Set<InvoicingKey> keys) throws DaoException;

    Set<InvoicePaymentEventId> getExistingEvents(List<CashFlowLink> links);

}
