package dev.vality.newway.dao.dominant.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.dominant.iface.DominantDao;
import dev.vality.newway.domain.Tables;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DominantDaoImpl extends AbstractGenericDao implements DominantDao {

    public DominantDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long getLastVersionId() throws DaoException {
        Query query = getDslContext().select(DSL.max(DSL.field("version_id"))).from(
                getDslContext().select(Tables.CALENDAR.VERSION_ID.max().as("version_id")).from(Tables.CALENDAR)
                        .unionAll(getDslContext().select(Tables.CATEGORY.VERSION_ID.max().as("version_id")).from(Tables.CATEGORY))
                        .unionAll(getDslContext().select(Tables.CURRENCY.VERSION_ID.max().as("version_id")).from(Tables.CURRENCY))
                        .unionAll(getDslContext().select(Tables.INSPECTOR.VERSION_ID.max().as("version_id")).from(Tables.INSPECTOR))
                        .unionAll(getDslContext().select(Tables.PAYMENT_INSTITUTION.VERSION_ID.max().as("version_id"))
                                .from(Tables.PAYMENT_INSTITUTION))
                        .unionAll(getDslContext().select(Tables.PAYMENT_METHOD.VERSION_ID.max().as("version_id"))
                                .from(Tables.PAYMENT_METHOD))
                        .unionAll(getDslContext().select(Tables.PAYOUT_METHOD.VERSION_ID.max().as("version_id"))
                                .from(Tables.PAYOUT_METHOD))
                        .unionAll(getDslContext().select(Tables.PROVIDER.VERSION_ID.max().as("version_id")).from(Tables.PROVIDER))
                        .unionAll(getDslContext().select(Tables.PROXY.VERSION_ID.max().as("version_id")).from(Tables.PROXY))
                        .unionAll(getDslContext().select(Tables.TERMINAL.VERSION_ID.max().as("version_id")).from(Tables.TERMINAL))
                        .unionAll(getDslContext().select(Tables.TERM_SET_HIERARCHY.VERSION_ID.max().as("version_id"))
                                .from(Tables.TERM_SET_HIERARCHY))
                        .unionAll(getDslContext().select(Tables.WITHDRAWAL_PROVIDER.VERSION_ID.max().as("version_id"))
                                .from(Tables.WITHDRAWAL_PROVIDER))
                        .unionAll(getDslContext().select(Tables.PAYMENT_ROUTING_RULE.VERSION_ID.max().as("version_id"))
                                .from(Tables.PAYMENT_ROUTING_RULE))
        );
        return fetchOne(query, Long.class);
    }
}
