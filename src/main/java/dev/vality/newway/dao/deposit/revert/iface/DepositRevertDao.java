package dev.vality.newway.dao.deposit.revert.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.DepositRevert;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface DepositRevertDao extends GenericDao {

    Optional<Long> save(DepositRevert revert) throws DaoException;

    DepositRevert get(String depositId, String revertId) throws DaoException;

    void updateNotCurrent(Long depositId) throws DaoException;

}
