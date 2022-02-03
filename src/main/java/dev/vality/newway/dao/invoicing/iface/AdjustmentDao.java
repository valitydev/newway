package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Adjustment;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface AdjustmentDao extends GenericDao {

    Optional<Long> save(Adjustment adjustment) throws DaoException;

    Adjustment get(String invoiceId, String paymentId, String adjustmentId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

}
