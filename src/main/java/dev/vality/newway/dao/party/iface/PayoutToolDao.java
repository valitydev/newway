package dev.vality.newway.dao.party.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.PayoutTool;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface PayoutToolDao extends GenericDao {
    void save(List<PayoutTool> payoutToolList) throws DaoException;

    List<PayoutTool> getByCntrctId(Long cntrctId) throws DaoException;
}
