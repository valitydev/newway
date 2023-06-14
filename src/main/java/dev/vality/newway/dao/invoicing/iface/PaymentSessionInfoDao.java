package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.PaymentSessionInfo;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface PaymentSessionInfoDao extends GenericDao {

    void saveBatch(List<PaymentSessionInfo> paymentStatusInfos) throws DaoException;

}