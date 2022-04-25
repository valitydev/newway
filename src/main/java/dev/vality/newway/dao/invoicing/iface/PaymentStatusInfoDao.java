package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.PaymentStatusInfo;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.model.InvoicingKey;

import java.util.List;
import java.util.Set;

public interface PaymentStatusInfoDao extends GenericDao {

    void saveBatch(List<PaymentStatusInfo> paymentStatusInfos) throws DaoException;

    PaymentStatusInfo get(String invoiceId, String paymentId) throws DaoException;

    void switchCurrent(Set<InvoicingKey> invoicesSwitchIds) throws DaoException;
}