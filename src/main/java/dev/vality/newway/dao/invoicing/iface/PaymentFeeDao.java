package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.PaymentFee;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.model.InvoicingKey;

import java.util.List;
import java.util.Set;

public interface PaymentFeeDao extends GenericDao {

    void saveBatch(List<PaymentFee> paymentFees) throws DaoException;

    PaymentFee get(String invoiceId, String paymentId) throws DaoException;

    void switchCurrent(Set<InvoicingKey> invoicingKeys) throws DaoException;

}
