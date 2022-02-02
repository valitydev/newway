package dev.vality.newway.dao.dominant.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.domain.tables.pojos.Proxy;
import dev.vality.newway.domain.tables.records.ProxyRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ProxyDaoImpl extends AbstractGenericDao implements DomainObjectDao<Proxy, Integer> {

    public ProxyDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(Proxy proxy) throws DaoException {
        ProxyRecord proxyRecord = getDslContext().newRecord(Tables.PROXY, proxy);
        Query query = getDslContext().insertInto(Tables.PROXY).set(proxyRecord).returning(Tables.PROXY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        executeOne(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateNotCurrent(Integer proxyId) throws DaoException {
        Query query = getDslContext().update(Tables.PROXY).set(Tables.PROXY.CURRENT, false)
                .where(Tables.PROXY.PROXY_REF_ID.eq(proxyId).and(Tables.PROXY.CURRENT));
        executeOne(query);
    }
}
