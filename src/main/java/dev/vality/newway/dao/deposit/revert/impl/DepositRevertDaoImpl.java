package dev.vality.newway.dao.deposit.revert.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.deposit.revert.iface.DepositRevertDao;
import dev.vality.newway.domain.tables.pojos.DepositRevert;
import dev.vality.newway.domain.tables.records.DepositRevertRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@Component
public class DepositRevertDaoImpl extends AbstractGenericDao implements DepositRevertDao {

    private final RowMapper<DepositRevert> depositRowMapper;

    @Autowired
    public DepositRevertDaoImpl(DataSource dataSource) {
        super(dataSource);
        depositRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT, DepositRevert.class);
    }

    @Override
    public Optional<Long> save(DepositRevert revert) throws DaoException {
        DepositRevertRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT, revert);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.DEPOSIT_ID, dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.REVERT_ID, dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @Override
    public DepositRevert get(String depositId, String revertId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT)
                .where(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.DEPOSIT_ID.eq(depositId)
                        .and(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.REVERT_ID.eq(revertId))
                        .and(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.CURRENT));
        return fetchOne(query, depositRowMapper);
    }

    @Override
    public void updateNotCurrent(Long revertId) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT).set(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.CURRENT, false)
                .where(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.ID.eq(revertId)
                        .and(dev.vality.newway.domain.tables.DepositRevert.DEPOSIT_REVERT.CURRENT));
        execute(query);
    }

}
