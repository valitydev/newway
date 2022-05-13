package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentStatusInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentStatusInfo;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.model.InvoicingKey;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.PaymentStatusInfo.PAYMENT_STATUS_INFO;

@Component
public class PaymentStatusInfoDaoImpl extends AbstractGenericDao implements PaymentStatusInfoDao {

    private final RowMapper<PaymentStatusInfo> rowMapper;

    public PaymentStatusInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.rowMapper = new RecordRowMapper<>(PAYMENT_STATUS_INFO, PaymentStatusInfo.class);
    }

    // TODO: try with resources?

    @Override
    public void saveBatch(List<PaymentStatusInfo> paymentStatusInfos) throws DaoException {
        List<Query> queries = paymentStatusInfos.stream()
                .map(statusInfo -> getDslContext().newRecord(PAYMENT_STATUS_INFO, statusInfo))
                .map(record -> getDslContext().insertInto(PAYMENT_STATUS_INFO)
                        .set(record)
                        .onConflict(
                                PAYMENT_STATUS_INFO.INVOICE_ID,
                                PAYMENT_STATUS_INFO.PAYMENT_ID,
                                PAYMENT_STATUS_INFO.SEQUENCE_ID,
                                PAYMENT_STATUS_INFO.CHANGE_ID
                        )
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public PaymentStatusInfo get(String invoiceId, String paymentId) throws DaoException {
        Query query = getDslContext().selectFrom(PAYMENT_STATUS_INFO)
                .where(PAYMENT_STATUS_INFO.INVOICE_ID.eq(invoiceId)
                        .and(PAYMENT_STATUS_INFO.PAYMENT_ID.eq(paymentId))
                        .and(PAYMENT_STATUS_INFO.CURRENT));
        return Optional.ofNullable(fetchOne(query, rowMapper)).orElseThrow(() ->
                new NotFoundException("PaymentStatusInfo not found, invoiceId=" + invoiceId + " paymentId=" + paymentId));
    }

    @Override
    public void switchCurrent(Set<InvoicingKey> invoicesSwitchIds) throws DaoException {
        invoicesSwitchIds.forEach(key -> {
            execute(getDslContext().update(PAYMENT_STATUS_INFO)
                    .set(PAYMENT_STATUS_INFO.CURRENT, false)
                    .where(PAYMENT_STATUS_INFO.INVOICE_ID.eq(key.getInvoiceId())
                            .and(PAYMENT_STATUS_INFO.PAYMENT_ID.eq(key.getPaymentId()))
                            .and(PAYMENT_STATUS_INFO.CURRENT))
            );
            execute(getDslContext().update(PAYMENT_STATUS_INFO)
                    .set(PAYMENT_STATUS_INFO.CURRENT, true)
                    .where(PAYMENT_STATUS_INFO.ID.eq(
                            DSL.select(DSL.max(PAYMENT_STATUS_INFO.ID))
                                    .from(PAYMENT_STATUS_INFO)
                                    .where(PAYMENT_STATUS_INFO.INVOICE_ID.eq(key.getInvoiceId())
                                            .and(PAYMENT_STATUS_INFO.PAYMENT_ID.eq(key.getPaymentId())))
                    ))
            );
        });
    }
}
