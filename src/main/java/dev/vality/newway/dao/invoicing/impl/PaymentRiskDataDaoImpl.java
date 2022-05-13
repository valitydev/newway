package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentRiskDataDao;
import dev.vality.newway.domain.tables.pojos.PaymentRiskData;
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

import static dev.vality.newway.domain.tables.PaymentRiskData.PAYMENT_RISK_DATA;

@Component
public class PaymentRiskDataDaoImpl extends AbstractGenericDao implements PaymentRiskDataDao {

    private final RowMapper<PaymentRiskData> rowMapper;

    public PaymentRiskDataDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.rowMapper = new RecordRowMapper<>(PAYMENT_RISK_DATA, PaymentRiskData.class);
    }

    @Override
    public void saveBatch(List<PaymentRiskData> paymentRiskDataList) throws DaoException {
        List<Query> queries = paymentRiskDataList.stream()
                .map(statusInfo -> getDslContext().newRecord(PAYMENT_RISK_DATA, statusInfo))
                .map(record -> getDslContext().insertInto(PAYMENT_RISK_DATA)
                        .set(record)
                        .onConflict(
                                PAYMENT_RISK_DATA.INVOICE_ID,
                                PAYMENT_RISK_DATA.PAYMENT_ID,
                                PAYMENT_RISK_DATA.SEQUENCE_ID,
                                PAYMENT_RISK_DATA.CHANGE_ID
                        )
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public PaymentRiskData get(String invoiceId, String paymentId) throws DaoException {
        Query query = getDslContext().selectFrom(PAYMENT_RISK_DATA)
                .where(PAYMENT_RISK_DATA.INVOICE_ID.eq(invoiceId)
                        .and(PAYMENT_RISK_DATA.PAYMENT_ID.eq(paymentId))
                        .and(PAYMENT_RISK_DATA.CURRENT)
                );
        return Optional.ofNullable(fetchOne(query, rowMapper)).orElseThrow(() ->
                new NotFoundException("PaymentRiskData not found, invoiceId=" + invoiceId + " paymentId=" + paymentId));
    }

    @Override
    public void switchCurrent(Set<InvoicingKey> invoicingKeys) throws DaoException {
        invoicingKeys.forEach(key -> {
            execute(getDslContext().update(PAYMENT_RISK_DATA)
                    .set(PAYMENT_RISK_DATA.CURRENT, false)
                    .where(PAYMENT_RISK_DATA.INVOICE_ID.eq(key.getInvoiceId())
                            .and(PAYMENT_RISK_DATA.PAYMENT_ID.eq(key.getPaymentId()))
                            .and(PAYMENT_RISK_DATA.CURRENT))
            );
            execute(getDslContext().update(PAYMENT_RISK_DATA)
                    .set(PAYMENT_RISK_DATA.CURRENT, true)
                    .where(PAYMENT_RISK_DATA.ID.eq(
                            DSL.select(DSL.max(PAYMENT_RISK_DATA.ID))
                                    .from(PAYMENT_RISK_DATA)
                                    .where(PAYMENT_RISK_DATA.INVOICE_ID.eq(key.getInvoiceId())
                                            .and(PAYMENT_RISK_DATA.PAYMENT_ID.eq(key.getPaymentId())))
                    ))
            );
        });
    }
}
