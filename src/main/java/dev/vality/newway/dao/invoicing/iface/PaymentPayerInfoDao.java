package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;

import dev.vality.newway.domain.tables.pojos.PaymentPayerInfo;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface PaymentPayerInfoDao extends GenericDao {

    void saveBatch(List<PaymentPayerInfo> payerInfos) throws DaoException;

    PaymentPayerInfo get(String invoiceId, String paymentId) throws DaoException;

}
