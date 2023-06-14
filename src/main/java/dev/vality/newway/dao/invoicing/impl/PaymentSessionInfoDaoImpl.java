package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.PaymentSessionInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentSessionInfo;
import dev.vality.newway.domain.tables.records.PaymentStatusInfoRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.PaymentStatusInfo.PAYMENT_STATUS_INFO;

@Component
public class PaymentSessionInfoDaoImpl extends AbstractGenericDao implements PaymentSessionInfoDao {

    public PaymentSessionInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveBatch(List<PaymentSessionInfo> paymentStatusInfos) throws DaoException {
        List<Query> queries = paymentStatusInfos.stream()
                .map(statusInfo -> getDslContext().newRecord(PAYMENT_STATUS_INFO, statusInfo))
                .map(this::prepareInsertQuery)
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    private Query prepareInsertQuery(PaymentStatusInfoRecord record) {
        return getDslContext().insertInto(PAYMENT_STATUS_INFO)
                .set(record)
                .onConflict(
                        PAYMENT_STATUS_INFO.INVOICE_ID,
                        PAYMENT_STATUS_INFO.PAYMENT_ID,
                        PAYMENT_STATUS_INFO.SEQUENCE_ID,
                        PAYMENT_STATUS_INFO.CHANGE_ID
                )
                .doNothing();
    }

}
