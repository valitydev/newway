package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.exception.DaoException;

import java.util.List;
import java.util.Set;

public interface InvoiceCartDao extends GenericDao {

    void save(List<InvoiceCart> invoiceCartList) throws DaoException;

    List<InvoiceCart> getByInvoiceId(String invoiceId) throws DaoException;

    Set<String> getExistingInvoiceIds(Set<String> invoiceIds) throws DaoException;

}
