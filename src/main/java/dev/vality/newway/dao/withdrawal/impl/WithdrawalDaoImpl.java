package dev.vality.newway.dao.withdrawal.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalDao;
import dev.vality.newway.domain.tables.pojos.Withdrawal;
import dev.vality.newway.domain.tables.records.WithdrawalRecord;
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
public class WithdrawalDaoImpl extends AbstractGenericDao implements WithdrawalDao {

    private final RowMapper<Withdrawal> withdrawalRowMapper;

    @Autowired
    public WithdrawalDaoImpl(DataSource dataSource) {
        super(dataSource);
        withdrawalRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL, Withdrawal.class);
    }

    @Override
    public Optional<Long> save(Withdrawal withdrawal) throws DaoException {
        WithdrawalRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL, withdrawal);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.WITHDRAWAL_ID, dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Withdrawal get(String withdrawalId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL)
                .where(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.WITHDRAWAL_ID.eq(withdrawalId)
                        .and(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.CURRENT));
        return Optional.ofNullable(fetchOne(query, withdrawalRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Withdrawal not found, withdrawalId='%s'", withdrawalId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL).set(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.CURRENT, false)
                .where(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.ID.eq(id)
                        .and(dev.vality.newway.domain.tables.Withdrawal.WITHDRAWAL.CURRENT));
        execute(query);
    }
}
