package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface InvoiceDao extends GenericDao {

    void saveBatch(List<Invoice> invoices) throws DaoException;

    Invoice get(String invoiceId) throws DaoException;

}