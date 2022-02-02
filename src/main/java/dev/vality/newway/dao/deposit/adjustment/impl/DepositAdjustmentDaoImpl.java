package dev.vality.newway.dao.deposit.adjustment.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.deposit.adjustment.iface.DepositAdjustmentDao;
import dev.vality.newway.domain.tables.pojos.DepositAdjustment;
import dev.vality.newway.domain.tables.records.DepositAdjustmentRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@Component
public class DepositAdjustmentDaoImpl extends AbstractGenericDao implements DepositAdjustmentDao {

    private final RowMapper<DepositAdjustment> depositRowMapper;

    @Autowired
    public DepositAdjustmentDaoImpl(DataSource dataSource) {
        super(dataSource);
        depositRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT, DepositAdjustment.class);
    }

    @Override
    public Optional<Long> save(DepositAdjustment adjustment) throws DaoException {
        DepositAdjustmentRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT, adjustment);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.DEPOSIT_ID, dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.ADJUSTMENT_ID,
                        dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @Override
    public DepositAdjustment get(String depositId, String adjustmentId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT)
                .where(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.DEPOSIT_ID.eq(depositId)
                        .and(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.ADJUSTMENT_ID.eq(adjustmentId))
                        .and(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.CURRENT));
        return fetchOne(query, depositRowMapper);
    }

    @Override
    public void updateNotCurrent(Long adjustmentId) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT).set(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.CURRENT, false)
                .where(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.ID.eq(adjustmentId)
                        .and(dev.vality.newway.domain.tables.DepositAdjustment.DEPOSIT_ADJUSTMENT.CURRENT));
        execute(query);
    }

}
