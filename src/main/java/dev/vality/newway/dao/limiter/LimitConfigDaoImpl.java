package dev.vality.newway.dao.limiter;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.domain.tables.pojos.LimitConfig;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.newway.domain.Tables.LIMIT_CONFIG;

@Component
public class LimitConfigDaoImpl extends AbstractGenericDao implements LimitConfigDao {

    private final RowMapper<LimitConfig> limitConfigRowMapper;

    @Autowired
    public LimitConfigDaoImpl(DataSource dataSource) {
        super(dataSource);
        limitConfigRowMapper = new RecordRowMapper<>(LIMIT_CONFIG, LimitConfig.class);
    }

    @Override
    public Optional<Long> save(LimitConfig limitConfig) throws DaoException {
        var limitConfigRecord = getDslContext().newRecord(LIMIT_CONFIG, limitConfig);
        var query = getDslContext()
                .insertInto(LIMIT_CONFIG)
                .set(limitConfigRecord)
                .onConflict(LIMIT_CONFIG.LIMIT_CONFIG_ID, LIMIT_CONFIG.SEQUENCE_ID)
                .doNothing()
                .returning(LIMIT_CONFIG.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @Override
    public LimitConfig get(String limitConfigId) throws DaoException {
        var query = getDslContext().selectFrom(LIMIT_CONFIG)
                .where(LIMIT_CONFIG.LIMIT_CONFIG_ID.eq(limitConfigId).and(LIMIT_CONFIG.CURRENT));
        return Optional.ofNullable(fetchOne(query, limitConfigRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("LimitConfig not found, limitConfigId='%s'", limitConfigId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        var query = getDslContext().update(LIMIT_CONFIG).set(LIMIT_CONFIG.CURRENT, false)
                .where(LIMIT_CONFIG.ID.eq(id)
                        .and(LIMIT_CONFIG.CURRENT));
        executeOne(query);
    }
}
