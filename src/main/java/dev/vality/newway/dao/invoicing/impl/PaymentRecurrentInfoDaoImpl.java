package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentRecurrentInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentRecurrentInfo;
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

import static dev.vality.newway.domain.tables.PaymentRecurrentInfo.PAYMENT_RECURRENT_INFO;

@Component
public class PaymentRecurrentInfoDaoImpl extends AbstractGenericDao implements PaymentRecurrentInfoDao {
    
    private final RowMapper<PaymentRecurrentInfo> rowMapper;
    
    public PaymentRecurrentInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.rowMapper = new RecordRowMapper<>(PAYMENT_RECURRENT_INFO, PaymentRecurrentInfo.class);
    }

    @Override
    public void saveBatch(List<PaymentRecurrentInfo> paymentRecurrentInfos) throws DaoException {
        List<Query> queries = paymentRecurrentInfos.stream()
                .map(statusInfo -> getDslContext().newRecord(PAYMENT_RECURRENT_INFO, statusInfo))
                .map(record -> getDslContext().insertInto(PAYMENT_RECURRENT_INFO)
                        .set(record)
                        .onConflict(
                                PAYMENT_RECURRENT_INFO.INVOICE_ID,
                                PAYMENT_RECURRENT_INFO.SEQUENCE_ID,
                                PAYMENT_RECURRENT_INFO.CHANGE_ID
                        )
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public PaymentRecurrentInfo get(String invoiceId, String paymentId) throws DaoException {
        Query query = getDslContext().selectFrom(PAYMENT_RECURRENT_INFO)
                .where(PAYMENT_RECURRENT_INFO.INVOICE_ID.eq(invoiceId)
                        .and(PAYMENT_RECURRENT_INFO.PAYMENT_ID.eq(paymentId))
                );
        return Optional.ofNullable(fetchOne(query, rowMapper)).orElseThrow(() ->
                new NotFoundException("PaymentPayerInfo not found, invoiceId=" + invoiceId + " paymentId=" + paymentId));
    }

    @Override
    public void switchCurrent(Set<InvoicingKey> invoicesSwitchIds) throws DaoException {
        invoicesSwitchIds.forEach(key -> {
            execute(getDslContext().update(PAYMENT_RECURRENT_INFO)
                    .set(PAYMENT_RECURRENT_INFO.CURRENT, false)
                    .where(PAYMENT_RECURRENT_INFO.INVOICE_ID.eq(key.getInvoiceId())
                            .and(PAYMENT_RECURRENT_INFO.PAYMENT_ID.eq(key.getPaymentId()))
                            .and(PAYMENT_RECURRENT_INFO.CURRENT))
            );
            execute(getDslContext().update(PAYMENT_RECURRENT_INFO)
                    .set(PAYMENT_RECURRENT_INFO.CURRENT, true)
                    .where(PAYMENT_RECURRENT_INFO.ID.eq(
                            DSL.select(DSL.max(PAYMENT_RECURRENT_INFO.ID))
                                    .from(PAYMENT_RECURRENT_INFO)
                                    .where(PAYMENT_RECURRENT_INFO.INVOICE_ID.eq(key.getInvoiceId())
                                            .and(PAYMENT_RECURRENT_INFO.PAYMENT_ID.eq(key.getPaymentId())))
                    ))
            );
        });
    }
}
