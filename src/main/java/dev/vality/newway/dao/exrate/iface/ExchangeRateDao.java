package dev.vality.newway.dao.exrate.iface;

import dev.vality.dao.DaoException;
import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.ExRate;

import java.util.List;

public interface ExchangeRateDao extends GenericDao {
    void saveBatch(List<ExRate> exchangeRates) throws DaoException;
    ExRate findBySourceSymbolicCode(String symbolicCode);
}
