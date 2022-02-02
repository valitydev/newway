package dev.vality.newway.dao.dominant.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.domain.tables.pojos.PaymentInstitution;
import dev.vality.newway.domain.tables.records.PaymentInstitutionRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class PaymentInstitutionDaoImpl extends AbstractGenericDao
        implements DomainObjectDao<PaymentInstitution, Integer> {

    public PaymentInstitutionDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(PaymentInstitution paymentInstitution) throws DaoException {
        PaymentInstitutionRecord paymentInstitutionRecord =
                getDslContext().newRecord(Tables.PAYMENT_INSTITUTION, paymentInstitution);
        Query query = getDslContext().insertInto(Tables.PAYMENT_INSTITUTION).set(paymentInstitutionRecord)
                .returning(Tables.PAYMENT_INSTITUTION.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        executeOne(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateNotCurrent(Integer paymentInstitutionId) throws DaoException {
        Query query = getDslContext().update(Tables.PAYMENT_INSTITUTION).set(Tables.PAYMENT_INSTITUTION.CURRENT, false)
                .where(Tables.PAYMENT_INSTITUTION.PAYMENT_INSTITUTION_REF_ID.eq(paymentInstitutionId)
                        .and(Tables.PAYMENT_INSTITUTION.CURRENT));
        executeOne(query);
    }
}
