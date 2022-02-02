package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.AdjustmentDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.domain.tables.pojos.Adjustment;
import dev.vality.newway.domain.tables.records.AdjustmentRecord;
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
public class AdjustmentDaoImpl extends AbstractGenericDao implements AdjustmentDao {

    private final RowMapper<Adjustment> adjustmentRowMapper;

    @Autowired
    public AdjustmentDaoImpl(DataSource dataSource) {
        super(dataSource);
        adjustmentRowMapper = new RecordRowMapper<>(Tables.ADJUSTMENT, Adjustment.class);
    }

    @Override
    public Optional<Long> save(Adjustment adjustment) throws DaoException {
        AdjustmentRecord record = getDslContext().newRecord(Tables.ADJUSTMENT, adjustment);
        Query query = getDslContext().insertInto(Tables.ADJUSTMENT)
                .set(record)
                .onConflict(Tables.ADJUSTMENT.INVOICE_ID, Tables.ADJUSTMENT.SEQUENCE_ID, Tables.ADJUSTMENT.CHANGE_ID)
                .doNothing()
                .returning(Tables.ADJUSTMENT.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Adjustment get(String invoiceId, String paymentId, String adjustmentId) throws DaoException {
        Query query = getDslContext().selectFrom(Tables.ADJUSTMENT)
                .where(Tables.ADJUSTMENT.INVOICE_ID.eq(invoiceId)
                        .and(Tables.ADJUSTMENT.PAYMENT_ID.eq(paymentId))
                        .and(Tables.ADJUSTMENT.ADJUSTMENT_ID.eq(adjustmentId))
                        .and(Tables.ADJUSTMENT.CURRENT));

        return Optional.ofNullable(fetchOne(query, adjustmentRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Adjustment not found, invoiceId='%s', paymentId='%s', adjustmentId='%s'",
                                invoiceId, paymentId, adjustmentId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(Tables.ADJUSTMENT).set(Tables.ADJUSTMENT.CURRENT, false).where(Tables.ADJUSTMENT.ID.eq(id));
        executeOne(query);
    }
}
