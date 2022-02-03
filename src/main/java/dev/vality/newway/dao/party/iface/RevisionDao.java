package dev.vality.newway.dao.party.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.exception.DaoException;

public interface RevisionDao extends GenericDao {

    void saveShopsRevision(String partyId, long revision) throws DaoException;

    void saveContractsRevision(String partyId, long revision) throws DaoException;

    void saveContractorsRevision(String partyId, long revision) throws DaoException;
}
