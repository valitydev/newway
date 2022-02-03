package dev.vality.newway.dao.dominant.impl;

import com.zaxxer.hikari.HikariDataSource;
import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.domain.tables.pojos.PaymentRoutingRule;
import dev.vality.newway.domain.tables.records.PaymentRoutingRuleRecord;
import dev.vality.newway.exception.DaoException;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
public class PaymentRoutingRulesDaoImpl extends AbstractGenericDao
        implements DomainObjectDao<PaymentRoutingRule, Integer> {

    public PaymentRoutingRulesDaoImpl(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(PaymentRoutingRule routingRule) throws DaoException {
        PaymentRoutingRuleRecord record = getDslContext().newRecord(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE, routingRule);
        Query query = getDslContext()
                .insertInto(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE)
                .set(record)
                .returning(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        executeOne(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateNotCurrent(Integer ruleId) throws DaoException {
        Query query = getDslContext()
                .update(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE)
                .set(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE.CURRENT, false)
                .where(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE.RULE_REF_ID.eq(ruleId).and(dev.vality.newway.domain.tables.PaymentRoutingRule.PAYMENT_ROUTING_RULE.CURRENT));
        execute(query);
    }
}
