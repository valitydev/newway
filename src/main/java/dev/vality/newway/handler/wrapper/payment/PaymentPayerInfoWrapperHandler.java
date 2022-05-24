package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentPayerInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentPayerInfo;
import dev.vality.newway.handler.wrapper.WrapperHandler;
import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PaymentPayerInfoWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentPayerInfoDao paymentPayerInfoDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentPayerInfo)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentPayerInfo> paymentPayerInfos = wrappers.stream()
                .map(PaymentWrapper::getPaymentPayerInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        paymentPayerInfoDao.saveBatch(paymentPayerInfos);
    }
}
