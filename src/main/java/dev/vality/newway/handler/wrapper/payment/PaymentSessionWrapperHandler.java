package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentSessionInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentSessionInfo;
import dev.vality.newway.handler.wrapper.WrapperHandler;
import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PaymentSessionWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentSessionInfoDao paymentSessionInfoDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentSessionInfo)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentSessionInfo> payments = wrappers.stream()
                .map(PaymentWrapper::getPaymentSessionInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        paymentSessionInfoDao.saveBatch(payments);
    }
}
