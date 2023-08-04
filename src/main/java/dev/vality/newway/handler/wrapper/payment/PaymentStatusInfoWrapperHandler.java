package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentStatusInfoDao;
import dev.vality.newway.domain.tables.pojos.PaymentStatusInfo;
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
public class PaymentStatusInfoWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentStatusInfoDao paymentStatusInfoDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentStatusInfo)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentWrapper> processableWrappers = wrappers.stream()
                .filter(paymentWrapper -> Objects.nonNull(paymentWrapper.getPaymentStatusInfo()))
                .collect(Collectors.toList());
        List<PaymentStatusInfo> paymentStatusInfos = processableWrappers.stream()
                .map(PaymentWrapper::getPaymentStatusInfo)
                .collect(Collectors.toList());
        paymentStatusInfoDao.saveBatch(paymentStatusInfos);
        paymentStatusInfoDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(processableWrappers));
    }
}
