package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.InvoiceDao;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.Invoice.INVOICE;

@Component
public class InvoiceDaoImpl extends AbstractGenericDao implements InvoiceDao {

    private final RowMapper<Invoice> invoiceRowMapper;

    @Autowired
    public InvoiceDaoImpl(DataSource dataSource) {
        super(dataSource);
        invoiceRowMapper = new RecordRowMapper<>(INVOICE, Invoice.class);
    }

    @Override
    public void saveBatch(List<Invoice> invoices) throws DaoException {
        List<Query> queries = invoices.stream()
                .map(invoice -> getDslContext().newRecord(INVOICE, invoice))
                .map(invoiceRecord -> getDslContext().insertInto(INVOICE)
                        .set(invoiceRecord)
                        .onConflict(INVOICE.INVOICE_ID, INVOICE.SEQUENCE_ID, INVOICE.CHANGE_ID)
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @NotNull
    @Override
    public Invoice get(String invoiceId) throws DaoException {
        Query query = getDslContext().selectFrom(INVOICE)
                .where(INVOICE.INVOICE_ID.eq(invoiceId).and(INVOICE.CURRENT));
        return Optional.ofNullable(fetchOne(query, invoiceRowMapper))
                .orElseThrow(
                        () -> new NotFoundException(String.format("Invoice not found, invoiceId='%s'", invoiceId)));
    }

    @Override
    public List<Invoice> getList(Set<String> invoiceIds) {
        Query query = getDslContext().selectFrom(INVOICE)
                .where(INVOICE.INVOICE_ID.in(invoiceIds).and(INVOICE.CURRENT));
        return fetch(query, invoiceRowMapper);
    }

    @Override
    public void switchCurrent(Set<String> invoiceIds) throws DaoException {
        invoiceIds.forEach(invoiceId -> {
            execute(getDslContext().update(INVOICE)
                    .set(INVOICE.CURRENT, false)
                    .where(INVOICE.INVOICE_ID.eq(invoiceId)
                            .and(INVOICE.CURRENT))
            );
            execute(getDslContext().update(INVOICE)
                    .set(INVOICE.CURRENT, true)
                    .where(INVOICE.ID.eq(
                            DSL.select(DSL.max(INVOICE.ID))
                                    .from(INVOICE)
                                    .where(INVOICE.INVOICE_ID.eq(invoiceId))
                    ))
            );
        });
    }
}
