package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.invoicing.iface.CashFlowDao;
import dev.vality.newway.domain.enums.AdjustmentCashFlowType;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CashFlowDaoImpl extends AbstractGenericDao implements CashFlowDao {

    private final RowMapper<CashFlow> cashFlowRowMapper;

    public CashFlowDaoImpl(DataSource dataSource) {
        super(dataSource);
        cashFlowRowMapper = new RecordRowMapper<>(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW, CashFlow.class);
    }

    @Override
    public void save(List<CashFlow> cashFlows) throws DaoException {
        List<Query> queries = cashFlows.stream()
                .map(cashFlow -> getDslContext().newRecord(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW, cashFlow))
                .map(cashFlowRecord -> getDslContext().insertInto(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW).set(cashFlowRecord))
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public List<CashFlow> getByObjId(Long objId, PaymentChangeType paymentChangeType) throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW)
                .where(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW.OBJ_ID.eq(objId).and(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW.OBJ_TYPE.eq(paymentChangeType)));
        return fetch(query, cashFlowRowMapper);
    }

    @Override
    public List<CashFlow> getForAdjustments(Long adjId, AdjustmentCashFlowType adjustmentCashFlowType)
            throws DaoException {
        Query query = getDslContext().selectFrom(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW)
                .where(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW.OBJ_ID.eq(adjId).and(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW.OBJ_TYPE.eq(PaymentChangeType.adjustment))
                        .and(dev.vality.newway.domain.tables.CashFlow.CASH_FLOW.ADJ_FLOW_TYPE.eq(adjustmentCashFlowType)));
        return fetch(query, cashFlowRowMapper);
    }
}
