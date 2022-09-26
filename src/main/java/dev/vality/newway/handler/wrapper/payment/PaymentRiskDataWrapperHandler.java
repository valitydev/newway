package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentRiskDataDao;
import dev.vality.newway.domain.tables.pojos.PaymentRiskData;
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
public class PaymentRiskDataWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentRiskDataDao paymentRiskDataDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentRiskData)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentWrapper> processableWrappers = wrappers.stream()
                .filter(paymentWrapper -> Objects.nonNull(paymentWrapper.getPaymentRiskData()))
                .collect(Collectors.toList());
        List<PaymentRiskData> paymentRiskDataList = processableWrappers.stream()
                .map(PaymentWrapper::getPaymentRiskData)
                .collect(Collectors.toList());
        paymentRiskDataDao.saveBatch(paymentRiskDataList);
        paymentRiskDataDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(processableWrappers));
    }
}
