package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentDao;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.Payment.PAYMENT;

@Component
public class PaymentDaoImpl extends AbstractGenericDao implements PaymentDao {

    private final RowMapper<Payment> paymentRowMapper;

    @Autowired
    public PaymentDaoImpl(DataSource dataSource) {
        super(dataSource);
        paymentRowMapper = new RecordRowMapper<>(PAYMENT, Payment.class);
    }

    @Override
    public void saveBatch(List<Payment> payments) throws DaoException {
        List<Query> queries = payments.stream()
                .map(payment -> getDslContext().newRecord(PAYMENT, payment))
                .map(paymentRecord -> getDslContext().insertInto(PAYMENT)
                        .set(paymentRecord)
                        .onConflict(PAYMENT.INVOICE_ID, PAYMENT.PAYMENT_ID, PAYMENT.SEQUENCE_ID, PAYMENT.CHANGE_ID)
                        .doNothing()
                )
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @NotNull
    @Override
    public Payment get(String invoiceId, String paymentId) throws DaoException {
        Query query = getDslContext().selectFrom(PAYMENT)
                .where(PAYMENT.INVOICE_ID.eq(invoiceId)
                        .and(PAYMENT.PAYMENT_ID.eq(paymentId)));
        return Optional.ofNullable(fetchOne(query, paymentRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Payment not found, invoiceId='%s', paymentId='%s'", invoiceId, paymentId)));
    }

}
