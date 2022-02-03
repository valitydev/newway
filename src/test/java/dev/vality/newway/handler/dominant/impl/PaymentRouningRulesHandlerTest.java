package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.*;
import dev.vality.newway.dao.dominant.impl.PaymentRoutingRulesDaoImpl;
import dev.vality.newway.domain.tables.pojos.PaymentRoutingRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PaymentRouningRulesHandlerTest {

    @Mock
    private PaymentRoutingRulesDaoImpl paymentRoutingRulesDao;

    @Test
    public void convertToDatabaseObjectTest() {
        RoutingRulesObject paymentRoutingRulesObject = buildPaymentRoutingRulesObject();
        PaymentRoutingRulesHandler handler = new PaymentRoutingRulesHandler(paymentRoutingRulesDao);
        handler.setDomainObject(DomainObject.routing_rules(paymentRoutingRulesObject));

        PaymentRoutingRule paymentRoutingRule =
                handler.convertToDatabaseObject(paymentRoutingRulesObject, 1L, true);

        Assertions.assertNotNull(paymentRoutingRule);
        Assertions.assertEquals(paymentRoutingRule.getRuleRefId().intValue(), paymentRoutingRulesObject.getRef().getId());
        Assertions.assertEquals(paymentRoutingRule.getName(), paymentRoutingRulesObject.getData().getName());
        Assertions.assertEquals(paymentRoutingRule.getDescription(), paymentRoutingRulesObject.getData().getDescription());
        Assertions.assertNotNull(paymentRoutingRule.getRoutingDecisionsJson());
    }

    private RoutingRulesObject buildPaymentRoutingRulesObject() {
        List<RoutingCandidate> candidates = new ArrayList<>();
        Predicate predicate = new Predicate();
        predicate.setConstant(true);
        RoutingCandidate candidate = new RoutingCandidate()
                .setDescription("CN-1")
                .setAllowed(predicate)
                .setTerminal(new TerminalRef().setId(1234))
                .setWeight(12)
                .setPriority(432);
        candidates.add(candidate);
        RoutingDecisions paymentRoutingDecisions = new RoutingDecisions();
        paymentRoutingDecisions.setCandidates(candidates);
        return new RoutingRulesObject()
                .setRef(new RoutingRulesetRef().setId(123))
                .setData(new RoutingRuleset()
                        .setName("test")
                        .setDescription("some desc")
                        .setDecisions(paymentRoutingDecisions)
                );
    }

}
