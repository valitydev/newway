package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentDao;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.handler.wrapper.WrapperHandler;
import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PaymentWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentDao paymentDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPayment)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<Payment> payments = wrappers.stream()
                .map(PaymentWrapper::getPayment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        paymentDao.saveBatch(payments);
    }
}
