package dev.vality.newway.dao.dominant.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.domain.tables.pojos.Country;
import dev.vality.newway.domain.tables.records.CountryRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CountryDaoImpl extends AbstractGenericDao implements DomainObjectDao<Country, String> {

    public CountryDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(Country country) throws DaoException {
        CountryRecord countryRecord = getDslContext().newRecord(Tables.COUNTRY, country);
        Query query = getDslContext().insertInto(Tables.COUNTRY).set(countryRecord).returning(Tables.COUNTRY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        executeOne(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateNotCurrent(String countryId) throws DaoException {
        Query query = getDslContext().update(Tables.COUNTRY).set(Tables.COUNTRY.CURRENT, false)
                .where(Tables.COUNTRY.COUNTRY_REF_ID.eq(countryId).and(Tables.COUNTRY.CURRENT));
        executeOne(query);
    }
}
