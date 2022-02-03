package dev.vality.newway.dao.deposit.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Deposit;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface DepositDao extends GenericDao {

    Optional<Long> save(Deposit deposit) throws DaoException;

    Deposit get(String depositId) throws DaoException;

    void updateNotCurrent(Long depositId) throws DaoException;

}
