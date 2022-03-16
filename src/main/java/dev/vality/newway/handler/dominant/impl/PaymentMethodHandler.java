package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.PaymentMethodObject;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.dao.dominant.impl.PaymentMethodDaoImpl;
import dev.vality.newway.domain.enums.PaymentMethodType;
import dev.vality.newway.handler.dominant.AbstractDominantHandler;
import dev.vality.newway.util.PaymentMethodUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class PaymentMethodHandler extends AbstractDominantHandler<PaymentMethodObject, dev.vality.newway.domain.tables.pojos.PaymentMethod, String> {

    private static final String SEPARATOR = ".";
    private static final String DEPRECATED = "_deprecated";
    private final PaymentMethodDaoImpl paymentMethodDao;

    public PaymentMethodHandler(PaymentMethodDaoImpl paymentMethodDao) {
        this.paymentMethodDao = paymentMethodDao;
    }

    @Override
    protected DomainObjectDao<dev.vality.newway.domain.tables.pojos.PaymentMethod, String> getDomainObjectDao() {
        return paymentMethodDao;
    }

    @Override
    protected PaymentMethodObject getTargetObject() {
        return getDomainObject().getPaymentMethod();
    }

    @Override
    protected String getTargetObjectRefId() {
        var paymentMethod = wrapPaymentMethod(getTargetObject().getRef().getId());

        Optional<String> paymentMethodRefId = PaymentMethodUtils.getPaymentMethodRefIdByBankCard(paymentMethod)
                .or(() -> PaymentMethodUtils.getPaymentMethodRefIdByPaymentTerminal(paymentMethod))
                .or(() -> PaymentMethodUtils.getPaymentMethodRefIdByDigitalWallet(paymentMethod))
                .or(() -> PaymentMethodUtils.getPaymentMethodRefIdByCryptoCurrency(paymentMethod))
                .or(() -> PaymentMethodUtils.getPaymentMethodRefIdByMobile(paymentMethod))
                .or(() -> PaymentMethodUtils.getPaymentMethodRefIdByGeneric(paymentMethod));

        if (paymentMethodRefId.isEmpty()) {
            throw new IllegalArgumentException("Unknown payment method: " + paymentMethod.get().get().getSetField());
        }

        return getPaymentType(getTargetObject()) + SEPARATOR + paymentMethodRefId.get();
    }

    private Supplier<Optional<dev.vality.damsel.domain.PaymentMethod>> wrapPaymentMethod(
            @NotNull dev.vality.damsel.domain.PaymentMethod paymentMethod) {
        return () -> Optional.of(paymentMethod);
    }

    private String getPaymentType(PaymentMethodObject pmObj) {
        return pmObj.getRef().getId().getSetField().getFieldName().replaceAll(DEPRECATED, "");
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetPaymentMethod();
    }

    @Override
    public dev.vality.newway.domain.tables.pojos.PaymentMethod convertToDatabaseObject(
            PaymentMethodObject paymentMethodObject,
            Long versionId,
            boolean current) {
        dev.vality.newway.domain.tables.pojos.PaymentMethod paymentMethod = new dev.vality.newway.domain.tables.pojos.PaymentMethod();
        paymentMethod.setVersionId(versionId);
        paymentMethod.setPaymentMethodRefId(getTargetObjectRefId());
        var data = paymentMethodObject.getData();
        paymentMethod.setName(data.getName());
        paymentMethod.setDescription(data.getDescription());
        paymentMethod.setType(
                Enum.valueOf(
                        PaymentMethodType.class,
                        getPaymentType(paymentMethodObject)
                )
        );
        paymentMethod.setCurrent(current);
        return paymentMethod;
    }
}
