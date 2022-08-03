package dev.vality.newway.dao.dominant.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.dominant.iface.DominantDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static org.jooq.impl.DSL.max;

@Component
public class DominantDaoImpl extends AbstractGenericDao implements DominantDao {

    public DominantDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long getLastVersionId() throws DaoException {
        Query query = getDslContext().select(max(DSL.field("max"))).from(
                getDslContext().select(max(Tables.CALENDAR.VERSION_ID)).from(Tables.CALENDAR)
                        .unionAll(getDslContext().select(max(Tables.CATEGORY.VERSION_ID)).from(Tables.CATEGORY))
                        .unionAll(getDslContext().select(max(Tables.COUNTRY.VERSION_ID)).from(Tables.COUNTRY))
                        .unionAll(getDslContext().select(max(Tables.CURRENCY.VERSION_ID)).from(Tables.CURRENCY))
                        .unionAll(getDslContext().select(max(Tables.INSPECTOR.VERSION_ID)).from(Tables.INSPECTOR))
                        .unionAll(getDslContext().select(max(Tables.PAYMENT_INSTITUTION.VERSION_ID))
                                .from(Tables.PAYMENT_INSTITUTION))
                        .unionAll(getDslContext().select(max(Tables.PAYMENT_METHOD.VERSION_ID))
                                .from(Tables.PAYMENT_METHOD))
                        .unionAll(getDslContext().select(max(Tables.PAYOUT_METHOD.VERSION_ID))
                                .from(Tables.PAYOUT_METHOD))
                        .unionAll(getDslContext().select(max(Tables.PROVIDER.VERSION_ID)).from(Tables.PROVIDER))
                        .unionAll(getDslContext().select(max(Tables.PROXY.VERSION_ID)).from(Tables.PROXY))
                        .unionAll(getDslContext().select(max(Tables.TERMINAL.VERSION_ID)).from(Tables.TERMINAL))
                        .unionAll(getDslContext().select(max(Tables.TERM_SET_HIERARCHY.VERSION_ID))
                                .from(Tables.TERM_SET_HIERARCHY))
                        .unionAll(getDslContext().select(max(Tables.TRADE_BLOC.VERSION_ID))
                                .from(Tables.TRADE_BLOC))
                        .unionAll(getDslContext().select(max(Tables.WITHDRAWAL_PROVIDER.VERSION_ID))
                                .from(Tables.WITHDRAWAL_PROVIDER))
                        .unionAll(getDslContext().select(max(Tables.PAYMENT_ROUTING_RULE.VERSION_ID))
                                .from(Tables.PAYMENT_ROUTING_RULE))
        );
        return fetchOne(query, Long.class);
    }
}
