package dev.vality.newway.dao.withdrawal.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.enums.FistfulCashFlowChangeType;
import dev.vality.newway.domain.tables.pojos.FistfulCashFlow;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface FistfulCashFlowDao extends GenericDao {

    void save(List<FistfulCashFlow> fistfulCashFlowList) throws DaoException;

    List<FistfulCashFlow> getByObjId(Long objId, FistfulCashFlowChangeType cashFlowChangeType) throws DaoException;

}
