package dev.vality.newway.dao.limiter;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.LimitConfig;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface LimitConfigDao extends GenericDao {

    Optional<Long> save(LimitConfig limitConfig) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

}
