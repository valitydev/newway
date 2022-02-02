package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.ChargebackDao;
import dev.vality.newway.domain.tables.pojos.Chargeback;
import dev.vality.newway.domain.tables.records.ChargebackRecord;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class ChargebackDaoImpl extends AbstractGenericDao implements ChargebackDao {

    private final RowMapper<Chargeback> chargebackRowMapper;

    @Autowired
    public ChargebackDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.chargebackRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK, Chargeback.class);
    }

    @Override
    public Optional<Long> save(Chargeback chargeback) throws DaoException {
        ChargebackRecord chargebackRecord = getDslContext().newRecord(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK, chargeback);
        Query query = getDslContext().insertInto(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK)
                .set(chargebackRecord)
                .onConflict(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.INVOICE_ID, dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.SEQUENCE_ID, dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.CHANGE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Chargeback get(String invoiceId, String paymentId, String chargebackId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK)
                .where(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.INVOICE_ID.eq(invoiceId)
                        .and(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.PAYMENT_ID.eq(paymentId))
                        .and(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.CHARGEBACK_ID.eq(chargebackId))
                        .and(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.CURRENT));

        return Optional.ofNullable(fetchOne(query, chargebackRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Chargeback not found, invoiceId='%s', paymentId='%s', chargebackId='%s'",
                                invoiceId, paymentId, chargebackId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK).set(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.CURRENT, false).where(dev.vality.newway.domain.tables.Chargeback.CHARGEBACK.ID.eq(id));
        executeOne(query);
    }
}
