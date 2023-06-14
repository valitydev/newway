package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.invoicing.iface.PaymentSessionInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentSessionInfo;
import dev.vality.newway.domain.tables.records.PaymentSessionInfoRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.tables.PaymentSessionInfo.PAYMENT_SESSION_INFO;

@Component
public class PaymentSessionInfoDaoImpl extends AbstractGenericDao implements PaymentSessionInfoDao {

    public PaymentSessionInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveBatch(List<PaymentSessionInfo> paymentSessionInfos) throws DaoException {
        List<Query> queries = paymentSessionInfos.stream()
                .map(statusInfo -> getDslContext().newRecord(PAYMENT_SESSION_INFO, statusInfo))
                .map(this::prepareInsertQuery)
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    private Query prepareInsertQuery(PaymentSessionInfoRecord record) {
        return getDslContext().insertInto(PAYMENT_SESSION_INFO)
                .set(record)
                .onConflict(
                        PAYMENT_SESSION_INFO.INVOICE_ID,
                        PAYMENT_SESSION_INFO.PAYMENT_ID,
                        PAYMENT_SESSION_INFO.SEQUENCE_ID,
                        PAYMENT_SESSION_INFO.CHANGE_ID
                )
                .doNothing();
    }

}
