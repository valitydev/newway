package dev.vality.newway.dao.deposit.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.deposit.iface.DepositDao;
import dev.vality.newway.domain.tables.pojos.Deposit;
import dev.vality.newway.domain.tables.records.DepositRecord;
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
public class DepositDaoImpl extends AbstractGenericDao implements DepositDao {

    private final RowMapper<Deposit> depositRowMapper;

    @Autowired
    public DepositDaoImpl(DataSource dataSource) {
        super(dataSource);
        depositRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Deposit.DEPOSIT, Deposit.class);
    }

    @Override
    public Optional<Long> save(Deposit deposit) throws DaoException {
        DepositRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.Deposit.DEPOSIT, deposit);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.Deposit.DEPOSIT)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.Deposit.DEPOSIT.DEPOSIT_ID, dev.vality.newway.domain.tables.Deposit.DEPOSIT.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Deposit.DEPOSIT.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Deposit get(String depositId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Deposit.DEPOSIT)
                .where(dev.vality.newway.domain.tables.Deposit.DEPOSIT.DEPOSIT_ID.eq(depositId)
                        .and(dev.vality.newway.domain.tables.Deposit.DEPOSIT.CURRENT));
        return Optional.ofNullable(fetchOne(query, depositRowMapper))
                .orElseThrow(
                        () -> new NotFoundException(String.format("Deposit not found, depositId='%s'", depositId)));
    }

    @Override
    public void updateNotCurrent(Long depositId) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Deposit.DEPOSIT).set(dev.vality.newway.domain.tables.Deposit.DEPOSIT.CURRENT, false)
                .where(dev.vality.newway.domain.tables.Deposit.DEPOSIT.ID.eq(depositId)
                        .and(dev.vality.newway.domain.tables.Deposit.DEPOSIT.CURRENT));
        execute(query);
    }

}
