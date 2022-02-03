package dev.vality.newway.dao.dominant.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.exception.DaoException;

public interface DomainObjectDao<T, I> extends GenericDao {

    Long save(T domainObject) throws DaoException;

    void updateNotCurrent(I objectId) throws DaoException;
}
