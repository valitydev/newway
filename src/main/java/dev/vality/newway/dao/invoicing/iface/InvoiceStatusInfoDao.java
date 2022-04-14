package dev.vality.newway.dao.invoicing.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import dev.vality.newway.exception.DaoException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface InvoiceStatusInfoDao extends GenericDao {

    void saveBatch(List<InvoiceStatusInfo> statuses) throws DaoException;

    InvoiceStatusInfo get(String invoiceId);

    List<InvoiceStatusInfo> getList(Set<String> invoiceIds);

    void switchCurrent(Collection<String> invoiceIds) throws DaoException;

}