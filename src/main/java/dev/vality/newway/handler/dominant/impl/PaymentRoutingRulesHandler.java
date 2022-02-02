package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.RoutingRulesObject;
import dev.vality.damsel.domain.RoutingRuleset;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.domain.tables.pojos.PaymentRoutingRule;
import dev.vality.newway.handler.dominant.AbstractDominantHandler;
import dev.vality.newway.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRoutingRulesHandler
        extends AbstractDominantHandler<RoutingRulesObject, PaymentRoutingRule, Integer> {

    private final DomainObjectDao<PaymentRoutingRule, Integer> paymentRoutingRulesDao;

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetRoutingRules();
    }

    @Override
    public PaymentRoutingRule convertToDatabaseObject(RoutingRulesObject rulesObject,
                                                      Long versionId,
                                                      boolean current) {

        PaymentRoutingRule paymentRoutingRule = new PaymentRoutingRule();
        paymentRoutingRule.setRuleRefId(rulesObject.getRef().getId());
        paymentRoutingRule.setVersionId(versionId);

        RoutingRuleset ruleset = rulesObject.getData();
        paymentRoutingRule.setName(ruleset.getName());
        paymentRoutingRule.setDescription(ruleset.getDescription());
        paymentRoutingRule.setRoutingDecisionsJson(JsonUtil.thriftBaseToJsonString(ruleset.getDecisions()));
        paymentRoutingRule.setCurrent(current);
        return paymentRoutingRule;
    }

    @Override
    protected DomainObjectDao<PaymentRoutingRule, Integer> getDomainObjectDao() {
        return paymentRoutingRulesDao;
    }

    @Override
    protected RoutingRulesObject getTargetObject() {
        return getDomainObject().getRoutingRules();
    }

    @Override
    protected Integer getTargetObjectRefId() {
        return getTargetObject().getRef().getId();
    }

}
