package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.InvoiceStatusInfoDao;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.beans.Transient;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.Tables.*;

@Component
public class InvoiceStatusInfoDaoImpl extends AbstractGenericDao implements InvoiceStatusInfoDao {

    private final RowMapper<InvoiceStatusInfo> rowMapper;

    public InvoiceStatusInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.rowMapper = new RecordRowMapper<>(INVOICE_STATUS_INFO, InvoiceStatusInfo.class);
    }

    @Transient
    @Override
    public void saveBatch(List<InvoiceStatusInfo> statuses) throws DaoException {
        batchExecute(statuses.stream()
                .map(status -> getDslContext().newRecord(INVOICE_STATUS_INFO, status))
                .map(invoiceStatusInfoRecord -> getDslContext().insertInto(INVOICE_STATUS_INFO)
                        .set(invoiceStatusInfoRecord)
                        .onConflict(
                                INVOICE_STATUS_INFO.INVOICE_ID,
                                INVOICE_STATUS_INFO.SEQUENCE_ID,
                                INVOICE_STATUS_INFO.CHANGE_ID)
                        .doNothing()
                )
                .collect(Collectors.toList())
        );
    }

    @Override
    public InvoiceStatusInfo get(String invoiceId) {
        Query query = getDslContext().selectFrom(INVOICE_STATUS_INFO)
                .where(INVOICE_STATUS_INFO.INVOICE_ID.eq(invoiceId)
                        .and(INVOICE_STATUS_INFO.CURRENT));
        return Optional.ofNullable(fetchOne(query, rowMapper)).orElseThrow(() ->
                new NotFoundException(String.format("InvoiceStatusInfo not found, invoiceId='%s'", invoiceId)));
    }

    @Override
    public List<InvoiceStatusInfo> getList(Set<String> invoiceIds) {
        Query query = getDslContext().selectFrom(INVOICE_STATUS_INFO)
                .where(INVOICE_STATUS_INFO.INVOICE_ID.in(invoiceIds)
                        .and(INVOICE_STATUS_INFO.CURRENT));
        return fetch(query, rowMapper);
    }


    @Override
    public void switchCurrent(Collection<String> invoiceIds) throws DaoException {
        invoiceIds.forEach(invoiceId -> {
                    execute(getDslContext().update(INVOICE_STATUS_INFO)
                            .set(INVOICE_STATUS_INFO.CURRENT, false)
                            .where(INVOICE_STATUS_INFO.INVOICE_ID.eq(invoiceId)
                                    .and(INVOICE_STATUS_INFO.CURRENT))
                    );
                    execute(getDslContext().update(INVOICE_STATUS_INFO)
                            .set(INVOICE_STATUS_INFO.CURRENT, true)
                            .where(INVOICE_STATUS_INFO.ID.eq(
                                    DSL.select(DSL.max(INVOICE_STATUS_INFO.ID))
                                            .from(INVOICE_STATUS_INFO)
                                            .where(INVOICE_STATUS_INFO.INVOICE_ID.eq(invoiceId))
                            ))
                    );
                }
        );
    }
}
