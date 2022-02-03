package dev.vality.newway.dao.withdrawal.session.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.WithdrawalSession;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface WithdrawalSessionDao extends GenericDao {

    Optional<Long> save(WithdrawalSession withdrawalSession) throws DaoException;

    WithdrawalSession get(String sessionId) throws DaoException;

    void updateNotCurrent(Long sessionId) throws DaoException;

}
