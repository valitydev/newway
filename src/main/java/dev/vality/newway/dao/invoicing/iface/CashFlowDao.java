package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.enums.AdjustmentCashFlowType;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface CashFlowDao extends GenericDao {

    void save(List<CashFlow> cashFlows) throws DaoException;

    List<CashFlow> getByObjId(Long objId, PaymentChangeType paymentchangetype) throws DaoException;

    List<CashFlow> getForAdjustments(Long adjId, AdjustmentCashFlowType adjustmentcashflowtype) throws DaoException;

}
