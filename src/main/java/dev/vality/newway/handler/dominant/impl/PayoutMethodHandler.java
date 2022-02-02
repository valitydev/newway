package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.PayoutMethodDefinition;
import dev.vality.damsel.domain.PayoutMethodObject;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.dao.dominant.impl.PayoutMethodDaoImpl;
import dev.vality.newway.domain.tables.pojos.PayoutMethod;
import dev.vality.newway.handler.dominant.AbstractDominantHandler;
import org.springframework.stereotype.Component;

@Component
public class PayoutMethodHandler extends AbstractDominantHandler<PayoutMethodObject, PayoutMethod, String> {

    private final PayoutMethodDaoImpl payoutMethodDao;

    public PayoutMethodHandler(PayoutMethodDaoImpl payoutMethodDao) {
        this.payoutMethodDao = payoutMethodDao;
    }

    @Override
    protected DomainObjectDao<PayoutMethod, String> getDomainObjectDao() {
        return payoutMethodDao;
    }

    @Override
    protected PayoutMethodObject getTargetObject() {
        return getDomainObject().getPayoutMethod();
    }

    @Override
    protected String getTargetObjectRefId() {
        return getTargetObject().getRef().getId().name();
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetPayoutMethod();
    }

    @Override
    public PayoutMethod convertToDatabaseObject(PayoutMethodObject payoutMethodObject, Long versionId,
                                                boolean current) {
        PayoutMethod payoutMethod = new PayoutMethod();
        payoutMethod.setVersionId(versionId);
        payoutMethod.setPayoutMethodRefId(getTargetObjectRefId());
        PayoutMethodDefinition data = payoutMethodObject.getData();
        payoutMethod.setName(data.getName());
        payoutMethod.setDescription(data.getDescription());
        payoutMethod.setCurrent(current);
        return payoutMethod;
    }
}
