package dev.vality.newway.dao.withdrawal.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.withdrawal.iface.FistfulCashFlowDao;
import dev.vality.newway.domain.enums.FistfulCashFlowChangeType;
import dev.vality.newway.domain.tables.pojos.FistfulCashFlow;
import dev.vality.newway.domain.tables.records.FistfulCashFlowRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class FistfulCashFlowDaoImpl extends AbstractGenericDao implements FistfulCashFlowDao {

    private final RowMapper<FistfulCashFlow> cashFlowRowMapper;

    public FistfulCashFlowDaoImpl(DataSource dataSource) {
        super(dataSource);
        cashFlowRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW, FistfulCashFlow.class);
    }

    @Override
    public void save(List<FistfulCashFlow> cashFlowList) throws DaoException {
        //todo: Batch insert
        for (FistfulCashFlow paymentCashFlow : cashFlowList) {
            FistfulCashFlowRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW, paymentCashFlow);
            Query query = getDslContext().insertInto(dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW).set(record);
            executeOne(query);
        }
    }

    @Override
    public List<FistfulCashFlow> getByObjId(Long objId, FistfulCashFlowChangeType cashFlowChangeType)
            throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW)
                .where(dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW.OBJ_ID.eq(objId))
                .and(dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW.OBJ_TYPE.eq(cashFlowChangeType));
        return fetch(query, cashFlowRowMapper);
    }

}
