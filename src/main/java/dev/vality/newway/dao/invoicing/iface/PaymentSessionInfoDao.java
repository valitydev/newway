package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.PaymentSessionInfo;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.model.InvoicingKey;

import java.util.List;
import java.util.Set;

public interface PaymentSessionInfoDao extends GenericDao {

    void saveBatch(List<PaymentSessionInfo> paymentStatusInfos) throws DaoException;

}