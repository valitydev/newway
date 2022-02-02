package dev.vality.newway.dao.identity.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.identity.iface.IdentityDao;
import dev.vality.newway.domain.tables.pojos.Identity;
import dev.vality.newway.domain.tables.records.IdentityRecord;
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
public class IdentityDaoImpl extends AbstractGenericDao implements IdentityDao {

    private final RowMapper<Identity> identityRowMapper;

    @Autowired
    public IdentityDaoImpl(DataSource dataSource) {
        super(dataSource);
        identityRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Identity.IDENTITY, Identity.class);
    }

    @Override
    public Optional<Long> save(Identity identity) throws DaoException {
        IdentityRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.Identity.IDENTITY, identity);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.Identity.IDENTITY)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.Identity.IDENTITY.IDENTITY_ID, dev.vality.newway.domain.tables.Identity.IDENTITY.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Identity.IDENTITY.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Identity get(String identityId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Identity.IDENTITY)
                .where(dev.vality.newway.domain.tables.Identity.IDENTITY.IDENTITY_ID.eq(identityId)
                        .and(dev.vality.newway.domain.tables.Identity.IDENTITY.CURRENT));

        return Optional.ofNullable(fetchOne(query, identityRowMapper))
                .orElseThrow(
                        () -> new NotFoundException(String.format("Identity not found, identityId='%s'", identityId)));
    }

    @Override
    public void updateNotCurrent(Long identityId) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Identity.IDENTITY).set(dev.vality.newway.domain.tables.Identity.IDENTITY.CURRENT, false)
                .where(
                        dev.vality.newway.domain.tables.Identity.IDENTITY.ID.eq(identityId)
                                .and(dev.vality.newway.domain.tables.Identity.IDENTITY.CURRENT)
                );
        execute(query);
    }
}
