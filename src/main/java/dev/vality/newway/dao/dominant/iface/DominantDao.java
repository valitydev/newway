package dev.vality.newway.dao.dominant.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.exception.DaoException;

public interface DominantDao extends GenericDao {
    Long getLastVersionId() throws DaoException;
    void updateLastVersionId(Long versionId) throws DaoException;
}
