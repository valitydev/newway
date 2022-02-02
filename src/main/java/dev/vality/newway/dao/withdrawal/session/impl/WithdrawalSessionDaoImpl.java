package dev.vality.newway.dao.withdrawal.session.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.withdrawal.session.iface.WithdrawalSessionDao;
import dev.vality.newway.domain.tables.pojos.WithdrawalSession;
import dev.vality.newway.domain.tables.records.WithdrawalSessionRecord;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class WithdrawalSessionDaoImpl extends AbstractGenericDao implements WithdrawalSessionDao {

    private final RowMapper<WithdrawalSession> withdrawalSessionRowMapper;

    @Autowired
    public WithdrawalSessionDaoImpl(@Qualifier("dataSource") DataSource dataSource) {
        super(dataSource);
        withdrawalSessionRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION, WithdrawalSession.class);
    }

    @Override
    public Optional<Long> save(WithdrawalSession withdrawalSession) throws DaoException {
        WithdrawalSessionRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION, withdrawalSession);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.WITHDRAWAL_SESSION_ID, dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public WithdrawalSession get(String sessionId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION)
                .where(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.WITHDRAWAL_SESSION_ID.eq(sessionId)
                        .and(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.CURRENT));
        return Optional.ofNullable(fetchOne(query, withdrawalSessionRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("WithdrawalSession not found, sessionId='%s'", sessionId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION).set(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.CURRENT, false)
                .where(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.ID.eq(id)
                        .and(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION.CURRENT));
        execute(query);
    }
}
