package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Refund;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface RefundDao extends GenericDao {

    Optional<Long> save(Refund refund) throws DaoException;

    Refund get(String invoiceId, String paymentId, String refundId) throws DaoException;

    void updateCommissions(Long rfndId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;
}
