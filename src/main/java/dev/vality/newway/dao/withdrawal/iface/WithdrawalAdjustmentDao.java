package dev.vality.newway.dao.withdrawal.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface WithdrawalAdjustmentDao extends GenericDao {

    Optional<Long> save(WithdrawalAdjustment withdrawalAdjustment) throws DaoException;

    WithdrawalAdjustment getByIds(String withdrawalId, String withdrawalAdjustmentId) throws DaoException;

    void updateNotCurrent(Long withdrawalAdjustmentId) throws DaoException;

}
