package dev.vality.newway.dao.party.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Contractor;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface ContractorDao extends GenericDao {

    Optional<Long> save(Contractor contractor) throws DaoException;

    Contractor get(String partyId, String contractorId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;
}
