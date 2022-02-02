package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.model.InvoicingKey;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PaymentDaoImpl extends AbstractGenericDao implements PaymentDao {

    private final RowMapper<Payment> paymentRowMapper;

    @Autowired
    public PaymentDaoImpl(DataSource dataSource) {
        super(dataSource);
        paymentRowMapper = new RecordRowMapper<>(Tables.PAYMENT, Payment.class);
    }

    @Override
    public void saveBatch(List<Payment> payments) throws DaoException {
        List<Query> queries = payments.stream()
                .map(payment -> getDslContext().newRecord(Tables.PAYMENT, payment))
                .map(paymentRecord -> getDslContext().insertInto(Tables.PAYMENT)
                        .set(paymentRecord)
                        .onConflict(Tables.PAYMENT.INVOICE_ID, Tables.PAYMENT.SEQUENCE_ID, Tables.PAYMENT.CHANGE_ID)
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public void updateBatch(List<Payment> payments) throws DaoException {
        List<Query> queries = payments.stream()
                .map(payment -> getDslContext().newRecord(Tables.PAYMENT, payment))
                .map(paymentRecord -> getDslContext().update(Tables.PAYMENT)
                        .set(paymentRecord)
                        .where(Tables.PAYMENT.ID.eq(paymentRecord.getId())))
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @NotNull
    @Override
    public Payment get(String invoiceId, String paymentId) throws DaoException {
        Query query = getDslContext().selectFrom(Tables.PAYMENT)
                .where(Tables.PAYMENT.INVOICE_ID.eq(invoiceId)
                        .and(Tables.PAYMENT.PAYMENT_ID.eq(paymentId))
                        .and(Tables.PAYMENT.CURRENT));

        return Optional.ofNullable(fetchOne(query, paymentRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Payment not found, invoiceId='%s', paymentId='%s'", invoiceId, paymentId)));
    }

    @Override
    public void switchCurrent(Collection<InvoicingKey> paymentsSwitchIds) throws DaoException {
        paymentsSwitchIds.forEach(ik ->
                this.getNamedParameterJdbcTemplate()
                        .update("update nw.payment set current = false " +
                                        "where invoice_id =:invoice_id and payment_id=:payment_id and current;" +
                                        "update nw.payment set current = true " +
                                        "where id = (select max(id) from nw.payment where invoice_id =:invoice_id " +
                                        "and payment_id=:payment_id);",
                                new MapSqlParameterSource("invoice_id", ik.getInvoiceId())
                                        .addValue("payment_id", ik.getPaymentId())));
    }
}
