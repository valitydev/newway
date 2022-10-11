package dev.vality.newway.dao.exrate.impl;

import dev.vality.dao.DaoException;
import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.exrate.iface.ExchangeRateDao;
import dev.vality.newway.domain.tables.pojos.Exrate;
import dev.vality.newway.domain.tables.records.ExrateRecord;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.newway.domain.tables.Exrate.EXRATE;

@Component
public class ExchangeRateDaoImpl extends AbstractGenericDao implements ExchangeRateDao {

    @Autowired
    public ExchangeRateDaoImpl(@Qualifier("dataSource") DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Exrate exchangeRate) throws DaoException {
        ExrateRecord record = getDslContext().newRecord(EXRATE, exchangeRate);
        Query query = getDslContext().insertInto(EXRATE).set(record)
                .onConflict(EXRATE.EVENT_ID)
                .doNothing()
                .returning(EXRATE.ID);
        execute(query);
    }
}
