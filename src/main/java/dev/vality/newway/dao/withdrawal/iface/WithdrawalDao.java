package dev.vality.newway.dao.withdrawal.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Withdrawal;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface WithdrawalDao extends GenericDao {

    Optional<Long> save(Withdrawal withdrawal) throws DaoException;

    Withdrawal get(String withdrawalId) throws DaoException;

    void updateNotCurrent(Long withdrawalId) throws DaoException;

}
