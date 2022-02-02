package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.RefundDao;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.Refund;
import dev.vality.newway.domain.tables.records.RefundRecord;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class RefundDaoImpl extends AbstractGenericDao implements RefundDao {

    private final RowMapper<Refund> refundRowMapper;

    @Autowired
    public RefundDaoImpl(DataSource dataSource) {
        super(dataSource);
        refundRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.Refund.REFUND, Refund.class);
    }

    @Override
    public Optional<Long> save(Refund refund) throws DaoException {
        RefundRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.Refund.REFUND, refund);
        Query query = getDslContext().insertInto(dev.vality.newway.domain.tables.Refund.REFUND)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.Refund.REFUND.INVOICE_ID, dev.vality.newway.domain.tables.Refund.REFUND.SEQUENCE_ID, dev.vality.newway.domain.tables.Refund.REFUND.CHANGE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.Refund.REFUND.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Refund get(String invoiceId, String paymentId, String refundId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.Refund.REFUND)
                .where(dev.vality.newway.domain.tables.Refund.REFUND.INVOICE_ID.eq(invoiceId)
                        .and(dev.vality.newway.domain.tables.Refund.REFUND.PAYMENT_ID.eq(paymentId))
                        .and(dev.vality.newway.domain.tables.Refund.REFUND.REFUND_ID.eq(refundId))
                        .and(dev.vality.newway.domain.tables.Refund.REFUND.CURRENT));

        return Optional.ofNullable(fetchOne(query, refundRowMapper))
                .orElseThrow(() -> new NotFoundException(String.format("Refund not found, " +
                        "invoiceId='%s', paymentId='%s', refundId='%s'", invoiceId, paymentId, refundId)));
    }

    @Override
    public void updateCommissions(Long rfndId) throws DaoException {
        MapSqlParameterSource params =
                new MapSqlParameterSource("rfndId", rfndId).addValue("objType", PaymentChangeType.refund.name());
        this.getNamedParameterJdbcTemplate().update(
                "UPDATE nw.refund SET fee = (SELECT nw.get_refund_fee(nw.cash_flow.*) " +
                        "FROM nw.cash_flow WHERE obj_id = :rfndId " +
                        "AND obj_type = CAST(:objType as nw.payment_change_type)), " +
                        "provider_fee = (SELECT nw.get_refund_provider_fee(nw.cash_flow.*) " +
                        "FROM nw.cash_flow WHERE obj_id = :rfndId " +
                        "AND obj_type = CAST(:objType as nw.payment_change_type)), " +
                        "external_fee = (SELECT nw.get_refund_external_fee(nw.cash_flow.*) " +
                        "FROM nw.cash_flow WHERE obj_id = :rfndId " +
                        "AND obj_type = CAST(:objType as nw.payment_change_type)) " +
                        "WHERE  id = :rfndId",
                params);
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.Refund.REFUND).set(dev.vality.newway.domain.tables.Refund.REFUND.CURRENT, false).where(dev.vality.newway.domain.tables.Refund.REFUND.ID.eq(id));
        executeOne(query);
    }
}
