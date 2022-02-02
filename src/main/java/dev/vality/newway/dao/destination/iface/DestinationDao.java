package dev.vality.newway.dao.destination.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Destination;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface DestinationDao extends GenericDao {

    Optional<Long> save(Destination destination) throws DaoException;

    Destination get(String destinationId) throws DaoException;

    void updateNotCurrent(Long destinationId) throws DaoException;

}
