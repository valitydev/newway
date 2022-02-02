package dev.vality.newway.dao.identity.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.identity.iface.ChallengeDao;
import dev.vality.newway.domain.tables.pojos.Challenge;
import dev.vality.newway.domain.tables.records.ChallengeRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@Component
public class ChallengeDaoImpl extends AbstractGenericDao implements ChallengeDao {

    private final RowMapper<Challenge> challengeRowMapper;

    @Autowired
    public ChallengeDaoImpl(DataSource dataSource) {
        super(dataSource);
        challengeRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Challenge.CHALLENGE, Challenge.class);
    }

    @Override
    public Optional<Long> save(Challenge challenge) throws DaoException {
        ChallengeRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.Challenge.CHALLENGE, challenge);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.Challenge.CHALLENGE)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.Challenge.CHALLENGE.IDENTITY_ID, dev.vality.newway.domain.tables.Challenge.CHALLENGE.CHALLENGE_ID, dev.vality.newway.domain.tables.Challenge.CHALLENGE.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Challenge.CHALLENGE.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @Override
    public Challenge get(String identityId, String challengeId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Challenge.CHALLENGE)
                .where(
                        dev.vality.newway.domain.tables.Challenge.CHALLENGE.IDENTITY_ID.eq(identityId)
                                .and(dev.vality.newway.domain.tables.Challenge.CHALLENGE.CHALLENGE_ID.eq(challengeId))
                                .and(dev.vality.newway.domain.tables.Challenge.CHALLENGE.CURRENT)
                );

        return fetchOne(query, challengeRowMapper);
    }

    @Override
    public void updateNotCurrent(String identityId, Long challengeId) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Challenge.CHALLENGE).set(dev.vality.newway.domain.tables.Challenge.CHALLENGE.CURRENT, false)
                .where(
                        dev.vality.newway.domain.tables.Challenge.CHALLENGE.ID.eq(challengeId)
                                .and(dev.vality.newway.domain.tables.Challenge.CHALLENGE.IDENTITY_ID.eq(identityId))
                                .and(dev.vality.newway.domain.tables.Challenge.CHALLENGE.CURRENT)
                );
        execute(query);
    }
}
