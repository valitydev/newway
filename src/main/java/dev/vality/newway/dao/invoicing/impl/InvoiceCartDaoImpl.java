package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.InvoiceCartDao;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.InvoiceCart.INVOICE_CART;

@Component
public class InvoiceCartDaoImpl extends AbstractGenericDao implements InvoiceCartDao {

    private final RowMapper<InvoiceCart> invoiceCartRowMapper;

    @Autowired
    public InvoiceCartDaoImpl(DataSource dataSource) {
        super(dataSource);
        invoiceCartRowMapper = new RecordRowMapper<>(INVOICE_CART, InvoiceCart.class);
    }

    @Override
    public void save(List<InvoiceCart> carts) throws DaoException {
        Set<String> deduplicatedInvoiceIds = getDeduplicatedInvoiceIds(carts);
        List<Query> queries = carts.stream()
                .filter(cart -> deduplicatedInvoiceIds.contains(cart.getInvoiceId()))
                .map(cart -> getDslContext().newRecord(INVOICE_CART, cart))
                .map(cartRecord -> getDslContext().insertInto(INVOICE_CART).set(cartRecord))
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public List<InvoiceCart> getByInvoiceId(String invoiceId) throws DaoException {
        Query query = getDslContext().selectFrom(INVOICE_CART)
                .where(INVOICE_CART.INVOICE_ID.eq(invoiceId));
        return fetch(query, invoiceCartRowMapper);
    }

    /**
     * Invoice cart can be written only once when Invoice is created.
     * Invoice cart cannot be changed, only way to change invoice cart is to cancel invoice and create new one.
     * @param carts List of InvoiceCarts
     * @return set of new invoice ids
     */
    private Set<String> getDeduplicatedInvoiceIds(List<InvoiceCart> carts) {
        Set<String> invoiceIds = carts.stream()
                .map(InvoiceCart::getInvoiceId)
                .collect(Collectors.toSet());
        invoiceIds.removeIf(invoiceId -> fetchOne(getDslContext()
                .select(DSL.count(INVOICE_CART.ID))
                .from(INVOICE_CART)
                .where(INVOICE_CART.INVOICE_ID.eq(invoiceId)), Integer.class) > 0);

        return invoiceIds;
    }
}
