package dev.vality.newway.dao.source.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Source;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface SourceDao extends GenericDao {

    Optional<Long> save(Source source) throws DaoException;

    Source get(String sourceId) throws DaoException;

    void updateNotCurrent(Long sourceId) throws DaoException;

}
