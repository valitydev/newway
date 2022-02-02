package dev.vality.newway.dao.recurrent.payment.tool.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.recurrent.payment.tool.iface.RecurrentPaymentToolDao;
import dev.vality.newway.domain.tables.pojos.RecurrentPaymentTool;
import dev.vality.newway.domain.tables.records.RecurrentPaymentToolRecord;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class RecurrentPaymentToolDaoImpl extends AbstractGenericDao implements RecurrentPaymentToolDao {

    private final RowMapper<RecurrentPaymentTool> recurrentPaymentToolRowMapper;

    public RecurrentPaymentToolDaoImpl(DataSource dataSource) {
        super(dataSource);
        recurrentPaymentToolRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL, RecurrentPaymentTool.class);
    }

    @Override
    public Optional<Long> save(RecurrentPaymentTool source) throws DaoException {
        RecurrentPaymentToolRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL, source);
        Query query = getDslContext().insertInto(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL)
                .set(record)
                .onConflict(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.RECURRENT_PAYMENT_TOOL_ID,
                        dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.SEQUENCE_ID,
                        dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.CHANGE_ID)
                .doNothing()
                .returning(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public RecurrentPaymentTool get(String recurrentPaymentToolId) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL)
                .where(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.RECURRENT_PAYMENT_TOOL_ID.eq(recurrentPaymentToolId)
                        .and(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.CURRENT));

        return Optional.ofNullable(fetchOne(query, recurrentPaymentToolRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Recurrent payment tool not found, sourceId='%s'", recurrentPaymentToolId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL).set(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.CURRENT, false)
                .where(dev.vality.newway.domain.tables.RecurrentPaymentTool.RECURRENT_PAYMENT_TOOL.ID.eq(id));
        executeOne(query);
    }


}

