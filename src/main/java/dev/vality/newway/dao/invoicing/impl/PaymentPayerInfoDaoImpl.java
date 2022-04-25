package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentPayerInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentPayerInfo;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.PaymentPayerInfo.PAYMENT_PAYER_INFO;

@Component
public class PaymentPayerInfoDaoImpl extends AbstractGenericDao implements PaymentPayerInfoDao {

    private final RowMapper<PaymentPayerInfo> rowMapper;

    public PaymentPayerInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.rowMapper = new RecordRowMapper<>(PAYMENT_PAYER_INFO, PaymentPayerInfo.class);
    }

    @Override
    public void saveBatch(List<PaymentPayerInfo> payerInfos) throws DaoException {
        List<Query> queries = payerInfos.stream()
                .map(statusInfo -> getDslContext().newRecord(PAYMENT_PAYER_INFO, statusInfo))
                .map(record -> getDslContext().insertInto(PAYMENT_PAYER_INFO)
                        .set(record)
                        .onConflict(
                                PAYMENT_PAYER_INFO.INVOICE_ID,
                                PAYMENT_PAYER_INFO.SEQUENCE_ID,
                                PAYMENT_PAYER_INFO.CHANGE_ID
                        )
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public PaymentPayerInfo get(String invoiceId, String paymentId) throws DaoException {
        Query query = getDslContext().selectFrom(PAYMENT_PAYER_INFO)
                .where(PAYMENT_PAYER_INFO.INVOICE_ID.eq(invoiceId)
                        .and(PAYMENT_PAYER_INFO.PAYMENT_ID.eq(paymentId)));
        return Optional.ofNullable(fetchOne(query, rowMapper)).orElseThrow(() ->
                new NotFoundException("PaymentPayerInfo not found, invoiceId=" + invoiceId + " paymentId=" + paymentId));
    }
}
