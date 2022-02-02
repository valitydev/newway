package dev.vality.newway.dao.destination.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.destination.iface.DestinationDao;
import dev.vality.newway.domain.tables.pojos.Destination;
import dev.vality.newway.domain.tables.records.DestinationRecord;
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
public class DestinationDaoImpl extends AbstractGenericDao implements DestinationDao {

    private final RowMapper<Destination> destinationRowMapper;

    @Autowired
    public DestinationDaoImpl(DataSource dataSource) {
        super(dataSource);
        destinationRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Destination.DESTINATION, Destination.class);
    }

    @Override
    public Optional<Long> save(Destination destination) throws DaoException {
        DestinationRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.Destination.DESTINATION, destination);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.Destination.DESTINATION)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.Destination.DESTINATION.DESTINATION_ID, dev.vality.newway.domain.tables.Destination.DESTINATION.SEQUENCE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Destination.DESTINATION.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Destination get(String destinationId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Destination.DESTINATION)
                .where(dev.vality.newway.domain.tables.Destination.DESTINATION.DESTINATION_ID.eq(destinationId)
                        .and(dev.vality.newway.domain.tables.Destination.DESTINATION.CURRENT));

        return Optional.ofNullable(fetchOne(query, destinationRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Destination not found, destinationId='%s'", destinationId)));
    }

    @Override
    public void updateNotCurrent(Long destinationId) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Destination.DESTINATION).set(dev.vality.newway.domain.tables.Destination.DESTINATION.CURRENT, false)
                .where(dev.vality.newway.domain.tables.Destination.DESTINATION.ID.eq(destinationId)
                        .and(dev.vality.newway.domain.tables.Destination.DESTINATION.CURRENT));
        execute(query);
    }
}
