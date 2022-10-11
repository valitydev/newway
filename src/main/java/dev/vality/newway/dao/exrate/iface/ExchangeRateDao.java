package dev.vality.newway.dao.exrate.iface;

import dev.vality.dao.DaoException;
import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Exrate;

public interface ExchangeRateDao extends GenericDao {
    void save(Exrate exchangeRate) throws DaoException;
}
