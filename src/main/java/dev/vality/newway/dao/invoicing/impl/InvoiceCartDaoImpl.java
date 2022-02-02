package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.InvoiceCartDao;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceCartDaoImpl extends AbstractGenericDao implements InvoiceCartDao {

    private final RowMapper<InvoiceCart> invoiceCartRowMapper;

    @Autowired
    public InvoiceCartDaoImpl(DataSource dataSource) {
        super(dataSource);
        invoiceCartRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.InvoiceCart.INVOICE_CART, InvoiceCart.class);
    }

    @Override
    public void save(List<InvoiceCart> carts) throws DaoException {
        List<Query> queries = carts.stream()
                .map(cart -> getDslContext().newRecord(dev.vality.newway.domain.tables.InvoiceCart.INVOICE_CART, cart))
                .map(cartRecord -> getDslContext().insertInto(dev.vality.newway.domain.tables.InvoiceCart.INVOICE_CART).set(cartRecord))
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public List<InvoiceCart> getByInvId(Long invId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.InvoiceCart.INVOICE_CART)
                .where(dev.vality.newway.domain.tables.InvoiceCart.INVOICE_CART.INV_ID.eq(invId));
        return fetch(query, invoiceCartRowMapper);
    }
}
