package dev.vality.newway.dao.rate.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Rate;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface RateDao extends GenericDao {

    Long save(Rate rate) throws DaoException;

    List<Long> getIds(String sourceId) throws DaoException;

    void updateNotCurrent(List<Long> ids) throws DaoException;

}
