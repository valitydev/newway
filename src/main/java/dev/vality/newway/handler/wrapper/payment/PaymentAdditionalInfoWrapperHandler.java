package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentAdditionalInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentAdditionalInfo;
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
public class PaymentAdditionalInfoWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentAdditionalInfoDao paymentAdditionalInfoDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentAdditionalInfo)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentAdditionalInfo> paymentAdditionalInfos = wrappers.stream()
                .map(PaymentWrapper::getPaymentAdditionalInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        paymentAdditionalInfoDao.saveBatch(paymentAdditionalInfos);
        paymentAdditionalInfoDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(wrappers));
    }
}
