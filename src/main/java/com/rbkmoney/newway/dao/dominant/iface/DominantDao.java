package com.rbkmoney.newway.dao.dominant.iface;

import dev.vality.dao.GenericDao;
import com.rbkmoney.newway.exception.DaoException;

public interface DominantDao extends GenericDao {
    Long getLastVersionId() throws DaoException;
}
