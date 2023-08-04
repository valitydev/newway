package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentRecurrentInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentRecurrentInfo;
import dev.vality.newway.handler.wrapper.WrapperHandler;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.util.PaymentWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PaymentRecurrentInfoWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentRecurrentInfoDao paymentRecurrentInfoDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentRecurrentInfo)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentWrapper> processableWrappers = wrappers.stream()
                .filter(paymentWrapper -> Objects.nonNull(paymentWrapper.getPaymentRecurrentInfo()))
                .collect(Collectors.toList());
        List<PaymentRecurrentInfo> paymentRecurrentInfos = processableWrappers.stream()
                .map(PaymentWrapper::getPaymentRecurrentInfo)
                .collect(Collectors.toList());
        paymentRecurrentInfoDao.saveBatch(paymentRecurrentInfos);
        paymentRecurrentInfoDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(processableWrappers));
    }
}
